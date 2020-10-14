package com.indrasoft.configservices.exception;

public class ConfigServicesException extends RuntimeException {

    private static final long serialVersionUID = -7864604160297181941L;

    /** 错误码 */
    protected final ErrCode errorCode;

    /**
     * 这个是和谐一些不必要的地方,冗余的字段
     * 尽量不要用
     */
    //    private String code;

    /**
     * 无参默认构造UNSPECIFIED
     */
    public ConfigServicesException() {
        super(ErrCodeEnum.UNSPECIFIED.getDesc());
        this.errorCode = ErrCodeEnum.UNSPECIFIED;
    }

    /**
     * 指定错误码构造通用异常
     *
     * @param errorCode 错误码
     */
    public ConfigServicesException(final ErrCode errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
    }

    /**
     * 指定详细描述构造通用异常
     *
     * @param detailedMessage 详细描述
     */
    public ConfigServicesException(final String detailedMessage) {
        super(detailedMessage);
        this.errorCode = ErrCodeEnum.UNSPECIFIED;
    }

    /**
     * 指定导火索构造通用异常
     *
     * @param t 导火索
     */
    public ConfigServicesException(final Throwable t) {
        super(t);
        this.errorCode = ErrCodeEnum.UNSPECIFIED;
    }

    /**
     * 构造通用异常
     *
     * @param errorCode 错误码
     */
    public ConfigServicesException(final ErrCode errorCode, final String deteilMsg) {
        super(errorCode.getDesc().contains("%s") ? String.format(errorCode.getDesc(), deteilMsg)
                                                 : errorCode.getDesc() + deteilMsg);

        this.errorCode = errorCode;
    }

    public void appendMsg(final String deteilMsg) {

    }

    public ConfigServicesException(final ErrCode errorCode, final Object... msg) {
        super(String.format(errorCode.getDesc(), msg));
        this.errorCode = errorCode;
    }

    /**
     * 构造通用异常
     *
     * @param errorCode 错误码
     * @param t         导火索
     */
    public ConfigServicesException(final ErrCode errorCode, final Throwable t) {
        super(errorCode.getDesc(), t);
        this.errorCode = errorCode;
    }

    /**
     * 构造通用异常
     *
     * @param detailedMessage 详细描述
     * @param t               导火索
     */
    public ConfigServicesException(final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = ErrCodeEnum.UNSPECIFIED;
    }

    /**
     * 构造通用异常
     *
     * @param errorCode       错误码
     * @param detailedMessage 详细描述
     * @param t               导火索
     */
    public ConfigServicesException(final ErrCode errorCode, final String detailedMessage,
            final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = errorCode;
    }

    /**
     * Getter method for property <tt>errorCode</tt>.
     *
     * @return property value of errorCode
     */
    public ErrCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        //        String s = getClass().getName();
        //        String message = getLocalizedMessage();
        //        return (message != null) ? (s + ": " + message) : s;
        return getLocalizedMessage();
    }

}