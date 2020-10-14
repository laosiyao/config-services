package com.indrasoft.configservices.template;

import com.indrasoft.configservices.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.Map;

/**
 * mvel表达式
 *
 * @Author laosiyao
 * @Date 2020/9/30 12:01 下午.
 */
public class MvelExpression implements Expression {

    private Serializable serializable;
    private String expression = StringUtils.EMPTY;

    private MvelExpression(String functions, String expression) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!StringUtils.isEmpty(functions)) {
            stringBuilder.append(functions).append(StringUtils.LF);
        }
        if (!StringUtils.isEmpty(expression)) {
            stringBuilder.append(expression);
            this.expression = expression;
        }
        serializable = MVEL.compileExpression(stringBuilder.toString(), MvelConst.PARSER_CONTEXT_LOCAL.get());
    }

    public static MvelExpression of(String functions, String expression) {
        return new MvelExpression(functions, expression);
    }

    @Override
    public boolean getResult(Map params) {
        Object resultObject;
        try {
            resultObject = MVEL.executeExpression(serializable, params);
            if (resultObject == null) {
                throw new ConfigServicesException(ErrCodeEnum.EXPRESSION_ERROR_RETURN_BOOLEAN, expression);
            }
            return (Boolean)resultObject;
        } catch (ClassCastException e) {
            throw new ConfigServicesException(ErrCodeEnum.EXPRESSION_ERROR_RETURN_BOOLEAN, expression);
        } catch (ConfigServicesException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigServicesException(ErrCodeEnum.EXPRESSION_ERROR, e.getMessage());
        }
    }

}
