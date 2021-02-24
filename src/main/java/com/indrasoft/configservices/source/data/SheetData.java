package com.indrasoft.configservices.source.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indrasoft.configservices.exception.*;
import com.indrasoft.configservices.source.verification.Verification;
import com.indrasoft.configservices.template.*;
import com.indrasoft.configservices.util.Util;
import org.apache.commons.collections4.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;

import java.io.File;
import java.util.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * @Author laosiyao
 * @Date 2020/9/22 3:49 下午.
 */
public class SheetData {

    private Logger logger = LoggerFactory.getLogger(SheetData.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    public static final String KEY_HEAD = "h:";
    public static final String KEY_FIELD_TYPE = "ftype";
    public static final String KEY_FIELD_NAME = "fname";
    public static final String KEY_CHECK = "check";
    public static final String KEY_PROFILE = "profile";
    static final String KEY_DATA = "d";

    private String mvlRootPath;
    private String outputRootPath;
    private String fileName;
    private SheetParams sheetParams;

    /**
     * key:Profile
     */
    private final Map<String, Attributes> attributesMap = new HashMap<>();
    private Attributes attribute;

    public SheetData(String mvlRootPath, String outputRootPath, String fileName, SheetParams sheetParams, List<List<String>> sourceLines)
            throws JsonProcessingException {
        this.mvlRootPath = mvlRootPath;
        this.outputRootPath = outputRootPath;
        this.fileName = fileName;
        this.sheetParams = sheetParams;

        parseAttributes(sourceLines);
    }

    private void parseAttributes(List<List<String>> sourceLines) {
        for (OutputConfig config : sheetParams.getOutput()) {
            long startAt = System.currentTimeMillis();
            if (CollectionUtils.isEmpty(config.getProfiles())) {
                logger.info(config.toString());
                attribute = getAttributes("", config, sourceLines);
            } else {
                for (String profile : config.getProfiles()) {
                    attributesMap.put(profile, getAttributes(profile, config, sourceLines));
                }
            }
        }
    }

    private Attributes getAttributes(String profile, OutputConfig outputConfig, List<List<String>> sourceLines) {
        Attributes attributes = new Attributes();
        attributes.setFileName(this.fileName);
        attributes.setSheetName(sheetParams.getTable());
        attributes.setParams(sheetParams.getParams());
        attributes.setProfile(profile);

        List<FieldInfo> fieldInfoList = parseFieldInfoConfig(sourceLines);
        List<List<CellData>> itemList = parseItemList(profile, sourceLines, fieldInfoList);

        List<Integer> removeIndex = new ArrayList<>();
        Set<String> profileFieldName = new HashSet<>();
        // 遍历，找出指定profile的字段名称
        for (FieldInfo fieldInfo : fieldInfoList) {
            if (!StringUtils.isEmpty(fieldInfo.getProfile())) {
                if (fieldInfo.getProfile().equalsIgnoreCase(profile)) {
                    profileFieldName.add(fieldInfo.getFieldName());
                }
            }
        }
        // 倒序遍历，找出需要删除的字段
        for (int index = fieldInfoList.size() - 1; index >= 0; index--) {
            FieldInfo fieldInfo = fieldInfoList.get(index);
            if (StringUtils.isEmpty(fieldInfo.getProfile())) {
                // 已有指定profile的同名字段
                if (profileFieldName.contains(fieldInfo.getFieldName())) {
                    removeIndex.add(index);
                }
            } else {
                // profile不一致
                if (!fieldInfo.getProfile().equalsIgnoreCase(profile)) {
                    removeIndex.add(index);
                }
            }
        }
        // 遍历，删除不需要的字段
        for (Integer index : removeIndex) {
            fieldInfoList.remove(index.intValue());
            for (List<CellData> row : itemList) {
                row.remove(index.intValue());
            }
        }

        attributes.setItemList(itemList);
        attributes.setFieldInfoList(fieldInfoList);
        return attributes;
    }

    private List<List<CellData>> parseItemList(String profile, List<List<String>> sourceLines, List<FieldInfo> fieldInfoList) {
        List<List<CellData>> itemList = new ArrayList<>();

        for (List<String> line : sourceLines) {
            if (line.size() <= 0) {
                continue;
            }
            String tag = line.get(0);
            if (!KEY_DATA.equalsIgnoreCase(tag)) {
                continue;
            }
            List<CellData> oneLine = new ArrayList<>();

            int index = 0;
            for (String cellContent : line.subList(1, line.size())) {
                if (fieldInfoList.size() <= index) {
                    break;
                }
                FieldInfo fieldInfo = fieldInfoList.get(index);

                //                // 过滤profile不一致的
                //                if (!StringUtils.isEmpty(fieldInfo.getProfile())) {
                //                    if (!fieldInfo.getProfile().equals(profile)) {
                //                        index++;
                //                        continue;
                //                    }
                //                }

                CellData cell = new CellData(cellContent, fieldInfo);
                oneLine.add(cell);
                index++;
            }
            itemList.add(oneLine);

            for (CellData cellData : oneLine) {
                for (CellData otherCell : oneLine) {
                    if (cellData.equals(otherCell)) {
                        continue;
                    }
                    cellData.getFieldList().put(otherCell.getFieldInfo().getFieldName(), otherCell);
                }
            }
        }
        return itemList;
    }

    private String getHeadContent(Map<String, List<String>> headMap, String keyHead, int index) {
        if (headMap.containsKey(keyHead)) {
            if (headMap.get(keyHead).size() > index) {
                return headMap.get(keyHead).get(index);
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * 解析头信息
     *
     * @param sourceLines
     * @param profile
     */
    private List<FieldInfo> parseFieldInfoConfig(List<List<String>> sourceLines) {
        List<FieldInfo> headerList = new ArrayList<>();
        for (List<String> line : sourceLines) {
            if (line.size() <= 0) {
                continue;
            }
            String tag = line.get(0);
            if (KEY_DATA.equalsIgnoreCase(tag)) {
                // 遇到data，直接退出
                break;
            }

            List<String> fields = line.subList(1, line.size());
            for (int index = 0; index < fields.size(); index++) {
                FieldInfo header;
                if (headerList.size() > index) {
                    header = headerList.get(index);
                } else {
                    header = new FieldInfo();
                    headerList.add(header);
                }

                String content = fields.get(index);
                if (tag.startsWith(KEY_HEAD)) {
                    // 自定义，例  h:desc  h:auth
                    String keyName = tag.substring(2, tag.length());
                    header.getCustomHeader().put(keyName, content);
                } else if (tag.startsWith(KEY_FIELD_TYPE)) {
                    header.setFieldType(content);
                } else if (tag.startsWith(KEY_FIELD_NAME)) {
                    // 字段名特殊处理
                    parseFieldName(content);
                    header.setFieldName(content);
                } else if (tag.startsWith(KEY_PROFILE)) {
                    header.setProfile(content);
                } else if (tag.startsWith(KEY_CHECK)) {
                    header.setCheckCode(content);
                }

            }

        }

        return headerList;

    }

    private void parseFieldName(String content) {

    }

    public void verify() throws Exception {

        String globalTemplate = sheetParams.loadFunctionMvlContent(mvlRootPath);

        if (attribute != null) {
            Verification.verify(attribute, globalTemplate);
        }
        for (Attributes attributes : attributesMap.values()) {
            Verification.verify(attributes, globalTemplate);
        }
    }

    public void export() throws Exception {
        for (OutputConfig config : sheetParams.getOutput()) {
            String mvlFilePath = config.getMvl();
            Template template = MvelTemplate.load(mvlRootPath + mvlFilePath);
            long startAt = System.currentTimeMillis();
            if (CollectionUtils.isEmpty(config.getProfiles())) {
                logger.info(config.toString());
                logger.info(attribute.toString());
                String out = template.getContent(attribute);
                logger.info(out);
                FileUtils.writeStringToFile(new File(outputRootPath + config.getOutput()), out);
            } else {
                for (String profile : config.getProfiles()) {
                    logger.info(config.toString() + "   this profile={}", profile);
                    Attributes attributes = attributesMap.get(profile);
                    logger.info(attributes.toString());
                    String out = template.getContent(attributes);
                    logger.info(out);
                    Map<String, Object> attributeMap = new HashMap<>();
                    attributeMap.put(KEY_PROFILE, profile);
                    String outputPath = MvelTemplate.of(config.getOutput()).getContent(attributeMap);
                    FileUtils.writeStringToFile(new File(outputRootPath + outputPath), out);
                }
            }
        }
    }

}
