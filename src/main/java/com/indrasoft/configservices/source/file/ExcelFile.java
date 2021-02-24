package com.indrasoft.configservices.source.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indrasoft.configservices.source.data.OutputConfig;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @Author laosiyao
 * @Date 2020/9/22 3:48 下午.
 */
public class ExcelFile extends AbstractFile {

    private static Logger logger = LoggerFactory.getLogger(ExcelFile.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String EXT_XLS = "xls";
    private static final String EXT_XLXS = "xlsx";

    public ExcelFile(String mvlRootPath, String outputRootPath, File file) throws Exception {
        super(mvlRootPath, outputRootPath, file);
    }

    @Override
    List<String> getSheetNames() throws Exception {
        List<String> sheetList = new ArrayList<>();
        Workbook workbook = getWorkbook(file.getPath());
        if (workbook == null) {
            return Collections.emptyList();
        }
        Iterator<Sheet> iterator = workbook.iterator();
        while (iterator.hasNext()) {
            Sheet sheet = iterator.next();
            sheetList.add(sheet.getSheetName());
        }
        return sheetList;
    }

    @Override
    List<List<String>> readLines(String sheetName) throws IOException {
        Workbook workbook = getWorkbook(file.getPath());
        if (workbook == null) {
            return Collections.emptyList();
        }
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            return Collections.emptyList();
        }

        List<List<String>> list = new ArrayList<>();
        // 遍历excel表的行
        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            List<String> rowList = new ArrayList<>();
            // 遍历excel表的列
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    break;
                }
                cell.setCellType(CellType.STRING);
                rowList.add(cell.getStringCellValue().trim().replace(StringUtils.LF, StringUtils.EMPTY));
            }
            list.add(rowList);
        }

        return list;
    }

    public static Workbook getWorkbook(String filePath) throws IOException {
        try (InputStream is = new FileInputStream(filePath)) {
            if (FilenameUtils.isExtension(filePath, EXT_XLS)) {
                return new HSSFWorkbook(is);
            } else if (FilenameUtils.isExtension(filePath, EXT_XLXS)) {
                return new XSSFWorkbook(is);
            } else {
                return null;
            }
        }
    }

    public static List<Sheet> getSheets(String filePath) throws IOException {
        String extString = FilenameUtils.getExtension(filePath);
        Workbook workbook = null;
        try (InputStream is = new FileInputStream(filePath)) {
            if (FilenameUtils.isExtension(filePath, EXT_XLS)) {
                workbook = new HSSFWorkbook(is);
            } else if (FilenameUtils.isExtension(filePath, EXT_XLXS)) {
                workbook = new XSSFWorkbook(is);
            } else {
                return Collections.emptyList();
            }
            List<Sheet> sheetList = new ArrayList<>();
            Iterator<Sheet> iterator = workbook.iterator();
            while (iterator.hasNext()) {
                Sheet sheet = iterator.next();
                Row row = sheet.getRow(0);
                if (row != null) {
                    Cell cell = row.getCell(0);
                    if (cell != null) {
                        cell.setCellType(CellType.STRING);
                        String content = cell.getStringCellValue();
                        try {
                            OBJECT_MAPPER.readValue(content, OutputConfig.class);
                        } catch (JsonProcessingException e) {
                            logger.error("{}", e);
                            continue;
                        }
                        sheetList.add(sheet);
                    }
                }
            }
            return sheetList;
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

    }

}
