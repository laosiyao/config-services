package com.indrasoft.configservices.source.manager;

import com.indrasoft.configservices.exception.*;
import com.indrasoft.configservices.source.file.*;
import org.slf4j.*;

import java.io.File;
import java.util.*;

/**
 * @Author laosiyao
 * @Date 2020/9/22 3:39 下午.
 */
public class FileManager {

    private List<AbstractFile> files = new ArrayList<>();
    private String mvlRootPath;
    private String outputRootPath;
    Logger logger = LoggerFactory.getLogger(FileManager.class);

    public FileManager(String mvlRootPath, String outputRootPath) {
        this.mvlRootPath = mvlRootPath;
        this.outputRootPath = outputRootPath;
    }

    public void add(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ConfigServicesException(ErrCodeEnum.FILE_NOT_EXSIT, filePath);
        }
        FileType type = FileType.getByFile(file);
        AbstractFile templateFile;
        switch (type) {
            case TSV:
                templateFile = new TsvFile(this.mvlRootPath, this.outputRootPath, file);
                break;
            case EXCEL:
                templateFile = new ExcelFile(this.mvlRootPath, this.outputRootPath, file);
                break;
            default:
                logger.error("unknown file {}", filePath);
                return;
            //                throw new ConfigServicesException(ErrCodeEnum.FILE_TYPE_INVALID, filePath);
        }
        files.add(templateFile);
    }

    public void verify() throws Exception {
        for (AbstractFile item : files) {
            item.verify();
        }
    }

    public void export() throws Exception {
        for (AbstractFile item : files) {
            item.export();
        }
    }

}
