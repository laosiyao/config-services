package com.indrasoft.configservices.source.data;

import com.indrasoft.configservices.exception.*;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * @Author laosiyao
 * @Date 2021/2/23 下午5:36.
 */
public class SheetParams {

    /**
     * sheetName
     */
    private String table;
    private Map<String, Object> params;
    private String functionMvl;
    private List<OutputConfig> output;

    public SheetParams(String table, Map<String, Object> params, String functionMvl,
            List<OutputConfig> output) {
        this.table = table;
        this.params = params;
        this.functionMvl = functionMvl;
        this.output = output;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String table;
        private Map<String, Object> params;
        private String functionMvl;
        private List<OutputConfig> output = new ArrayList<>();

        public Builder setTable(final String table) {
            this.table = table;
            return this;
        }

        public Builder setParams(final Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder setFunctionMvl(final String functionMvl) {
            this.functionMvl = functionMvl;
            return this;
        }

        public Builder addOutput(final OutputConfig output) {
            this.output.add(output);
            return this;
        }

        public SheetParams build() {
            return new SheetParams(this.table, this.params, this.functionMvl, this.output);
        }

    }

    public String getTable() {
        return defaultIfEmpty(table, EMPTY);
    }

    public Map<String, Object> getParams() {
        return Collections.unmodifiableMap(params);
    }

    private String getFunctionMvl() {
        return defaultIfEmpty(functionMvl, EMPTY);
    }

    public String loadFunctionMvlContent(String mvlRootPath) {
        if (isEmpty(getFunctionMvl())) {
            return EMPTY;
        }
        String mvlPath = mvlRootPath + "/" + getFunctionMvl();
        try {

            return FileUtils.readFileToString(new File(mvlPath));
        } catch (IOException e) {
            throw new ConfigServicesException(ErrCodeEnum.FILE_NOT_EXSIT, mvlPath);
        }
    }

    public List<OutputConfig> getOutput() {
        return Collections.unmodifiableList(output);
    }

}
