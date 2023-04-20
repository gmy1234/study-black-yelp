package com.hmdp.exception;


import com.hmdp.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 *  放造假项目运行过程中产生的异常，用于统一管理报错信息
 * @author gmy
 */

@Getter
@Setter
public class BusinessException extends RuntimeException {

    public static final int DEFAULT_FAULT_CODE = -1;

    private int code;

    private String message;

    public BusinessException(String message) {
        this(DEFAULT_FAULT_CODE, message);
    }

    public BusinessException(int code, String message) {
        this(code, message, new Throwable());
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BusinessException(StatusEnum statusEnum) {
        super(new Throwable());
        this.code = statusEnum.getIndex();
        this.message = statusEnum.getDesc();
    }

    public BusinessException(StatusEnum statusEnum, Throwable cause) {
        super(statusEnum.getDesc(), cause);
        this.code = statusEnum.getIndex();
        this.message = statusEnum.getDesc();
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
