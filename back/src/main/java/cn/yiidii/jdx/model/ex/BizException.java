package cn.yiidii.jdx.model.ex;

import lombok.Getter;

/**
 * BizException
 *
 * @author ed w
 * @since 1.0
 */
@Getter
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -1L;

    private String message;
    private int code;

    public BizException(String message) {
        super(message);
        this.code = -1;
        this.message = message;
    }
}
