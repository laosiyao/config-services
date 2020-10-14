package com.indrasoft.configservices.source.verification;

import com.indrasoft.configservices.util.Util;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @Author laosiyao
 * @Date 2020/9/29 2:33 下午.
 */
public enum FieldType {
    INT("int") {
        @Override
        boolean verify(String content) {
            return StringUtils.isNumeric(content);
        }
    },
    LONG("long") {
        @Override
        boolean verify(String content) {
            return StringUtils.isNumeric(content);
        }
    },
    FLOAT("float") {
        @Override
        boolean verify(String content) {
            return NumberUtils.isCreatable(content);
        }
    },
    BOOL("bool") {
        @Override
        boolean verify(String content) {
            return BooleanUtils.toBooleanObject(content) != null;
        }
    },
    JSON("json") {
        @Override
        boolean verify(String content) {
            return Util.isJson(content);
        }

    },
    ARRAY("array") {
        @Override
        boolean verify(String content) {
            return Util.isArray(content);
        }

    },

    STRING("string"),
    UNKNOWN("unknown"),

    ;

    private String type;

    FieldType(String type) {
        this.type = type;
    }

    boolean verify(String content) {
        return true;
    }

    String getType() {
        return type;
    }

    public static FieldType getType(String type) {
        for (FieldType fieldType : values()) {
            if (fieldType.getType().equalsIgnoreCase(type)) {
                return fieldType;
            }
        }
        return UNKNOWN;
    }

}
