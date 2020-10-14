package com.indrasoft.configservices.source.verification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indrasoft.configservices.exception.*;
import com.indrasoft.configservices.source.data.*;
import com.indrasoft.configservices.template.*;
import org.apache.commons.beanutils.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author laosiyao
 * @Date 2020/9/29 11:29 上午.
 */
public class Verification {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void verify(Attributes attributes, String globalTemplatePath) throws ConfigServicesException {
        List<FieldType> fieldTypeList = new ArrayList<>();
        List<Expression> expressionList = new ArrayList<>();

        attributes.getFieldInfoList().forEach(fieldInfo -> {
            String type = fieldInfo.getFieldType();
            fieldTypeList.add(FieldType.getType(type));
        });
        attributes.getFieldInfoList().forEach(fieldInfo -> {
            String checkCode = fieldInfo.getCheckCode();
            if (!StringUtils.isEmpty(checkCode)) {
                Expression expression = MvelExpression.of(globalTemplatePath, checkCode);
                expressionList.add(expression);
            } else {
                expressionList.add(null);
            }
        });

        attributes.getItemList().forEach(rowList -> {
                    for (int index = 0; index < rowList.size(); index++) {
                        CellData item = rowList.get(index);
                        String type = item.getFieldInfo().getFieldType();
                        String content = item.getContent();
                        String fileName = attributes.getFileName();
                        String sheetName = attributes.getSheetName();
                        if (fieldTypeList.size() > index) {
                            FieldType fieldType = fieldTypeList.get(index);
                            if (!fieldType.verify(content)) {
                                throw new ConfigServicesException(ErrCodeEnum.FORMAT_ERR_FIRLD_FORMAT, fileName, sheetName, content, type);
                            }
                        }
                        if (expressionList.size() > index && expressionList.get(index) != null) {
                            Expression expression = expressionList.get(index);

                            BeanMap beanmap = new BeanMap(item);
                            Map<String, Object> map = new HashMap<>();
                            for (Object key : beanmap.keySet()) {
                                map.put(key.toString(), beanmap.get(key.toString()));
                            }
                            if (!expression.getResult(map)) {
                                String checkCode = item.getFieldInfo().getCheckCode();
                                throw new ConfigServicesException(ErrCodeEnum.FORMAT_ERR_FIRLD_CHECK, fileName, sheetName, content, checkCode);
                            }
                        }
                    }

                }

        );
    }

}
