package com.indrasoft.configservices.source.data;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author laosiyao
 * @Date 2020/10/12 2:28 下午.
 */
public class FieldInfo {

    private String profile;
    private String fieldName;
    private String fieldType;
    private String checkCode;

    private Map<String, String> customHeader = new HashMap<>();

    public String getProfile() {
        if (profile == null) {
            return StringUtils.EMPTY;
        }
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Map<String, String> getCustomHeader() {
        return customHeader;
    }

    public void setCustomHeader(Map<String, String> customHeader) {
        this.customHeader = customHeader;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public FieldInfo(String profile, String fieldName, String fieldType, String checkCode, Map<String, String> customHeader) {
        this.profile = profile;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.checkCode = checkCode;
        this.customHeader = customHeader;
    }

    public FieldInfo() {

    }

}
