package com.indrasoft.configservices.source.data;

import java.util.*;

/**
 * @Author laosiyao
 * @Date 2020/10/12 2:36 下午.
 */
public class CellData {

    private String content;
    private FieldInfo fieldInfo;
    private Map<String, CellData> fieldList = new HashMap<>();

    public CellData(String content, FieldInfo fieldInfo) {
        this.content = content;
        this.fieldInfo = fieldInfo;
    }

    public CellData(String content, FieldInfo fieldInfo, Map<String, CellData> fieldList) {
        this.content = content;
        this.fieldInfo = fieldInfo;
        this.fieldList = fieldList;
    }

    public CellData() {
    }

    public Map<String, CellData> getFieldList() {
        return fieldList;
    }

    public void setFieldList(Map<String, CellData> fieldList) {
        this.fieldList = fieldList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

}
