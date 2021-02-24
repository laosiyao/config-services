package com.indrasoft.configservices.source.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indrasoft.configservices.exception.*;
import com.indrasoft.configservices.source.data.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @Author laosiyao
 * @Date 2020/9/22 4:03 下午.
 */
public abstract class AbstractFile {

    private static final String PARAMS = "params";
    private static final String FUNCTION_MVL = "functionMvl";
    private static final String OUTPUT = "output";
    private static final String TABLE = "table";
    private static final String INDEX = "index";
    private Logger logger = LoggerFactory.getLogger(AbstractFile.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected File file;
    private final List<SheetData> sheets = new ArrayList<>();

    public AbstractFile(String mvlRootPath, String outputRootPath, File file) throws Exception {
        this.file = file;
        Map<String, SheetParams> sheetParamsMap = null;
        for (String sheetName : getSheetNames()) {
            if (INDEX.equalsIgnoreCase(sheetName)) {
                List<List<String>> lines = readLines(sheetName);
                sheetParamsMap = analysisIndex(lines);
            }
        }

        if (sheetParamsMap == null) {
            throw new ConfigServicesException(ErrCodeEnum.NEED_INDEX_PAGE, file.getName());
        }

        for (String sheetName : getSheetNames()) {
            if (INDEX.equalsIgnoreCase(sheetName)) {
                continue;
            }
            if (!sheetParamsMap.containsKey(sheetName)) {
                continue;
            }
            List<List<String>> lines = readLines(sheetName);
            sheets.add(new SheetData(mvlRootPath, outputRootPath, file.getName(), sheetParamsMap.get(sheetName), lines));
        }
        if (sheets.isEmpty()) {
            throw new ConfigServicesException(ErrCodeEnum.NO_CONTENT_ERROR, file.getName());
        }

    }

    abstract List<String> getSheetNames() throws IOException, Exception;

    abstract List<List<String>> readLines(String sheetName) throws IOException;

    Map<String, SheetParams> analysisIndex(List<List<String>> lines) {
        Map<String, SheetParams> sheetConfigInfoMap = new HashMap<>();
        List<String> head = Collections.EMPTY_LIST;
        for (int rowIndex = 0; rowIndex < lines.size(); rowIndex++) {
            if (rowIndex == 0) {
                head = lines.get(rowIndex);
                continue;
            }
            SheetParams.Builder sheetConfigInfoBuilder = new SheetParams.Builder();
            List<String> rows = lines.get(rowIndex);
            for (int contentIndex = 0; contentIndex < rows.size(); contentIndex++) {
                String content = rows.get(contentIndex);
                if (content.isEmpty()) {
                    continue;
                }
                if (head.size() <= contentIndex) {
                    continue;
                }
                String field = head.get(contentIndex);
                try {
                    if (field.equalsIgnoreCase(TABLE)) {
                        sheetConfigInfoBuilder.setTable(content);
                    } else if (field.equalsIgnoreCase(PARAMS)) {
                        sheetConfigInfoBuilder.setParams(objectMapper.readValue(content, new TypeReference<Map<String, Object>>() {
                        }));
                    } else if (field.equalsIgnoreCase(FUNCTION_MVL)) {
                        sheetConfigInfoBuilder.setFunctionMvl(content);
                    } else if (field.equalsIgnoreCase(OUTPUT)) {
                        OutputConfig outputConfig = objectMapper.readValue(content, OutputConfig.class);
                        sheetConfigInfoBuilder.addOutput(outputConfig);
                    }
                } catch (JsonProcessingException e) {
                    logger.error("{} {}", content, e);
                    throw new ConfigServicesException(ErrCodeEnum.FILE_CONFIG_INVALID, content);
                }
                SheetParams sheetParams = sheetConfigInfoBuilder.build();
                sheetConfigInfoMap.put(sheetParams.getTable(), sheetParams);
            }
        }

        return sheetConfigInfoMap;
    }

    public void verify() throws Exception {
        for (SheetData sheet : sheets) {
            sheet.verify();
        }
    }

    public void export() throws Exception {
        for (SheetData sheet : sheets) {
            sheet.export();
        }
    }

}
