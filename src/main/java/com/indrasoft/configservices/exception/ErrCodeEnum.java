/**
 * @author: laosiyao
 * @date 2020年6月17日
 * @copyright IndraSoft
 */
package com.indrasoft.configservices.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * @author: laosiyao
 * @date 2020年6月17日
 * @copyright IndraSoft
 */
public enum ErrCodeEnum implements ErrCode {

    /** 未指明的异常 */
    UNSPECIFIED("-1", true, "不明异常，请联系管理员"),

    /**
     *
     */
    FILE_NOT_EXSIT("1001", false, "文件不存在 %s"),
    FILE_TYPE_INVALID("1002", false, "不能解析的文件类型 %s"),
    FILE_CONFIG_INVALID("1003", false, "头信息json配置异常 %s"),
    TEMPLATE_ERROR("1004", true, "模板运算错误 %s"),
    NO_CONTENT_ERROR("1005", false, "表 %s 没有可导出的内容"),

    /** 配置解析错误 */
    FORMAT_ERR_FIRLD_FORMAT("2001", false, "表:%s  sheet:%s  字段内容:%s  字段类型:%s 格式检查不通过"),
    FORMAT_ERR_FIRLD_CHECK("2002", false, "表:%s  sheet:%s  字段内容:%s 自定义检查不通过，检查公式 %s"),

    EXPRESSION_ERROR("2005", true, "表达式运算错误 %s"),
    EXPRESSION_ERROR_RETURN_BOOLEAN("2005", false, "表达式运算错误,必须返回boolean  表达式：%s"),

    FORMAT_ERR_FILE_CHARSET("2011", false, "文件 %s 编码错误:%s,必须是不带BOM的UTF-8文件"),

    ;

    private final String code;
    private final String desc;
    private final boolean printstack;

    private ErrCodeEnum(final String code, final boolean printstack, final String desc) {
        this.code = code;
        this.printstack = printstack;
        this.desc = desc;
    }

    /*
     * (non-Javadoc)
     *
     * @see def.exception.ErrCode#getCode()
     */
    @Override
    public String getCode() {
        // TODO Auto-generated method stub
        return code;
    }

    /*
     * (non-Javadoc)
     *
     * @see def.exception.ErrCode#getDesc()
     */
    @Override
    public String getDesc() {
        // TODO Auto-generated method stub
        return "errcode = [" + code + "] ,msg = " + desc + "";
    }

    @Override
    public Boolean isPrintStack() {
        return this.printstack;
    }

    public static ErrCodeEnum getByCode(String code) {
        for (ErrCodeEnum value : ErrCodeEnum.values()) {
            if (StringUtils.equals(code, value.getCode())) {
                return value;
            }
        }
        return UNSPECIFIED;
    }

    public static Boolean contains(String code) {
        for (ErrCodeEnum value : ErrCodeEnum.values()) {
            if (StringUtils.equals(code, value.getCode())) {
                return true;
            }
        }
        return false;
    }
}
