package com.indrasoft.configservices.source.file;

import com.indrasoft.configservices.exception.*;
import com.indrasoft.configservices.source.data.SheetData;

import java.io.*;
import java.util.*;

/**
 * @Author laosiyao
 * @Date 2020/9/22 4:03 下午.
 */
public abstract class AbstractFile {

    protected File file;
    private final List<SheetData> sheets = new ArrayList<>();

    public AbstractFile(String mvlRootPath, String outputRootPath, File file) throws Exception {
        this.file = file;
        for (String sheetName : getSheetNames()) {
            List<List<String>> lines = readLines(sheetName);
            sheets.add(new SheetData(mvlRootPath, outputRootPath, file.getName(), sheetName, lines));
        }
        if (sheets.isEmpty()) {
            throw new ConfigServicesException(ErrCodeEnum.NO_CONTENT_ERROR, file.getName());
        }
    }

    abstract List<String> getSheetNames() throws IOException, Exception;

    abstract List<List<String>> readLines(String sheetName) throws IOException;

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
