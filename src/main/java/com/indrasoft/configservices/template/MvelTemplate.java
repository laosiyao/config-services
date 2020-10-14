package com.indrasoft.configservices.template;

import com.indrasoft.configservices.exception.*;
import com.indrasoft.configservices.source.data.Attributes;
import org.apache.commons.io.IOUtils;
import org.mvel2.templates.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * mvel模板
 */
public class MvelTemplate implements Template {

    private CompiledTemplate expression;

    public static MvelTemplate load(String templatePath) throws Exception {
        InputStream in = null;
        String template = null;
        try {
            in = new FileInputStream(templatePath);
            template = IOUtils.toString(in, Charset.defaultCharset().name());
            return MvelTemplate.of(template);
        } catch (FileNotFoundException e) {
            throw new ConfigServicesException(ErrCodeEnum.FILE_NOT_EXSIT, templatePath);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static MvelTemplate of(String template) {
        return new MvelTemplate(template);
    }

    private MvelTemplate(String template) {
        expression = TemplateCompiler.compileTemplate(template, MvelConst.PARSER_CONTEXT_LOCAL.get());
    }

    @Override
    public String getContent(Attributes attributes) {
        try {
            return TemplateRuntime.execute(expression, attributes).toString();
        } catch (Exception e) {
            throw new ConfigServicesException(ErrCodeEnum.TEMPLATE_ERROR, e.getMessage());
        }
    }

    @Override
    public String getContent(Map attributes) {
        try {
            return TemplateRuntime.execute(expression, attributes).toString();
        } catch (Exception e) {
            throw new ConfigServicesException(ErrCodeEnum.TEMPLATE_ERROR, e.getMessage());
        }
    }

    //    public InputStream getContentStream(Map<String, Object> attributes) throws Exception {
    //        String template = getContent(attributes);
    //        return new ByteArrayInputStream(template.getBytes(Charset.defaultCharset().name()));
    //    }

}
