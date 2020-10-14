package com.indrasoft.configservices.template;

import com.indrasoft.configservices.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.mvel2.ParserContext;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author laosiyao
 * @Date 2020/9/30 12:16 下午.
 */
public class MvelConst {

    public final static ThreadLocal<ParserContext> PARSER_CONTEXT_LOCAL = new ThreadLocal<ParserContext>() {

        @Override
        protected ParserContext initialValue() {
            ParserContext context = new ParserContext();
            for (final Method method : Math.class.getMethods()) {
                context.addImport(method.getName(), method);
            }
            for (final Method method : MathEx.class.getMethods()) {
                context.addImport(method.getName(), method);
            }
            for (final Method method : FormulaUtils.class.getMethods()) {
                context.addImport(method.getName(), method);
            }
            for (final Method method : StringEscapeUtils.class.getMethods()) {
                context.addImport(method.getName(), method);
            }
            for (final Method method : Util.class.getMethods()) {
                context.addImport(method.getName(), method);
            }
            for (final Method method : StringUtils.class.getMethods()) {
                context.addImport(method.getName(), method);
            }
            for (final Method method : NumberUtils.class.getMethods()) {
                context.addImport(method.getName(), method);
            }

            context.addImport(ArrayList.class);
            context.addImport(HashSet.class);
            context.addImport(HashMap.class);
            return context;
        }

    };

}
