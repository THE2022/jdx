package cn.yiidii.jdx.model.ex;

import cn.yiidii.jdx.model.enums.ExceptionCode;
import lombok.Getter;

/**
 * UnauthorizedException
 *
 * @author ed w
 * @since 1.0
 */
@Getter
public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = -1L;

    private String message;
    private int code;

    public UnauthorizedException() {
        this.code = ExceptionCode.UNAUTHORIZED.getCode();
        this.message = ExceptionCode.UNAUTHORIZED.getMsg();
    }

    public UnauthorizedException(String msg) {
        this.message = msg;
    }
}
