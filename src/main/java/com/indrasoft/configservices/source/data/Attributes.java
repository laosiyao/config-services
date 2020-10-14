package com.indrasoft.configservices.source.data;

import java.util.*;

/**
 * 数据格式
 *
 * @Author laosiyao
 * @Date 2020/9/29 11:41 上午.
 */
public class Attributes {

    /**
     *
     */
    private String profile;
    /**
     * 自定义参数
     */
    private Map<String, Object> params;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 工作表名（excel）
     */
    private String sheetName;

    /**
     * 字段信息
     */
    private List<FieldInfo> fieldInfoList;

    /**
     * 具体内容
     */
    private List<List<CellData>> itemList;

    //    /**
    //     * 头信息
    //     * key=(fn,ft,以及自定义行)
    //     */
    //    private Map<String, List<String>> headMap;
    //    /**
    //     * 字段信息（头信息的变形）
    //     * key=(fn,ft,以及自定义行)
    //     */
    //    private List<Map<String, String>> fieldInfoList;
    //    /**
    //     * 内容（行、列）
    //     * key=(content，fn,ft,以及自定义行)
    //     */
    //    private List<List<Map<String, String>>> itemList;

    public Attributes(String profile, Map<String, Object> params, String fileName, String sheetName,
            List<FieldInfo> fieldInfoList, List<List<CellData>> itemList) {
        this.profile = profile;
        this.params = params;
        this.fileName = fileName;
        this.sheetName = sheetName;
        this.fieldInfoList = fieldInfoList;
        this.itemList = itemList;
    }

    public Attributes() {

    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<FieldInfo> getFieldInfoList() {
        return fieldInfoList;
    }

    public void setFieldInfoList(List<FieldInfo> fieldInfoList) {
        this.fieldInfoList = fieldInfoList;
    }

    public List<List<CellData>> getItemList() {
        return itemList;
    }

    public void setItemList(List<List<CellData>> itemList) {
        this.itemList = itemList;
    }

}
