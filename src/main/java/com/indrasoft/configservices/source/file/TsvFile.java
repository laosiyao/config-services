package com.indrasoft.configservices.source.file;

import com.indrasoft.configservices.exception.*;
import com.indrasoft.configservices.util.Util;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Author laosiyao
 * @Date 2020/9/22 3:40 下午.
 */
public class TsvFile extends AbstractFile {

    private static List<String> SHEET_LIST = Arrays.asList(StringUtils.EMPTY);

    public TsvFile(String mvlRootPath, String outputRootPath, File file) throws Exception {
        super(mvlRootPath, outputRootPath, file);
    }

    @Override
    List<String> getSheetNames() {
        // tsv没有sheet，只有一份数据
        return SHEET_LIST;
    }

    @Override
    List<List<String>> readLines(String sheetName) throws IOException {
        String sourceCharset = Util.getFilecharset(file);
        if (!Charset.defaultCharset().name().equalsIgnoreCase(sourceCharset)) {
            throw new ConfigServicesException(ErrCodeEnum.FORMAT_ERR_FILE_CHARSET, file.getName(), sourceCharset);
        }

        try (
                InputStreamReader fr = new InputStreamReader(new FileInputStream(file.getAbsolutePath()), Charset.defaultCharset());
                BufferedReader br = new BufferedReader(fr);
        ) {
            String line = "";
            List<List<String>> list = new ArrayList<>();
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                List<String> row = new ArrayList<String>();
                String[] arr = line.split("\t");
                for (String str : arr) {
                    row.add(str);
                }
                list.add(row);
            }
            return list;
        }
    }

}
