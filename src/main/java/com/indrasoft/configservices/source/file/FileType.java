package com.indrasoft.configservices.source.file;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.*;

/**
 * @Author laosiyao
 * @Date 2020/9/23 9:51 上午.
 */
public enum FileType {

    TSV("tsv"),

    EXCEL("xls", "xlsx"),

    UNKNWON(""),

    ;

    private List<String> suffixList;

    private FileType(String... suffixs) {
        this.suffixList = Collections.unmodifiableList(Arrays.asList(suffixs));
    }

    public List<String> getSuffixList() {
        return suffixList;
    }

    public static FileType getByFile(File file) {
        String extensionName = FilenameUtils.getExtension(file.getName()).toLowerCase();
        for (FileType type : FileType.values()) {
            if (type.suffixList.contains(extensionName)) {
                return type;
            }
        }
        return UNKNWON;
    }

}