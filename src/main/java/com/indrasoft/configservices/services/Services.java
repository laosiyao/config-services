package com.indrasoft.configservices.services;

import com.indrasoft.configservices.exception.*;
import com.indrasoft.configservices.source.manager.FileManager;
import com.indrasoft.configservices.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;

import java.io.File;
import java.util.Optional;

/**
 * @Author laosiyao
 * @Date 2020/9/22 4:15 下午.
 */
public class Services {

    private static Logger logger = LoggerFactory.getLogger(Services.class);

    public static Optional<String> execute(String mvlRootPath, String outputRootPath, String filePath) {
        try {

            FileManager manager = new FileManager(mvlRootPath, outputRootPath);

            File mainFile = new File(filePath);
            if (!mainFile.exists()) {
                throw new ConfigServicesException(ErrCodeEnum.FILE_NOT_EXSIT, filePath);
            }
            if (mainFile.isDirectory()) {
                for (File subFile : mainFile.listFiles()) {
                    if (subFile.isFile()) {
                        manager.add(subFile.getPath());
                    }
                }
            } else {
                manager.add(filePath);
            }

            manager.verify();
            manager.export();
            return Optional.empty();
        } catch (Exception e) {
            boolean printStackTrace = true;
            String showMsg = StringUtils.EMPTY;
            if (e instanceof ConfigServicesException) {
                showMsg = e.getMessage();
                logger.error(e.getMessage());
                ConfigServicesException emsg = (ConfigServicesException)e;
                if (!emsg.getErrorCode().isPrintStack()) {
                    printStackTrace = false;
                }
            } else {
                showMsg = new ConfigServicesException(e).getMessage();
                logger.error(showMsg);
                //                logger.error(new ConfigServicesException().getMessage());
            }
            if (printStackTrace) {
                showMsg += Util.getStackTrace(e.getStackTrace());
                logger.error(showMsg);
            }
            return Optional.of(showMsg);
        }

    }

    public static void main(String[] args) {
        execute("./src/test/resources/excels/Trunk/", "./src/test/resources/excels/Trunk/"
                , "./src/test/resources/excels/Trunk/config/J-角色-角色配置-Hero.xls");
        //        execute("./src/test/resources/excels/Trunk/", "./src/test/resources/excels/Trunk/config/testOutfit.tsv");
        //        execute("./src/test/resources/excels/Trunk/", "./src/test/resources/excels/Trunk/config/testFile.tsv");

    }

}
