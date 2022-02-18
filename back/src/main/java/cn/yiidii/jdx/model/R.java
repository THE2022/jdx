
package cn.yiidii.jdx.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 *  R
 *
 * @author ed w
 * @since 1.0
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;

    @Getter
    @Setter
    private String errMsg;

    @Getter
    @Setter
    private T data;

    @Getter
    @Setter
    private long timestamp;

    public static <T> R<T> ok() {
        return restResult(null, 0, null, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, 0, "处理成功", null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, 0, msg, null);
    }

    public static <T> R<T> failed() {
        return restResult(null,-1,  "处理失败", null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null,-1, msg, null);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data,-1,  "处理失败", null);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data,-1, msg, null);
    }

    public static <T> R<T> failed(int code, String msg) {
        return restResult(null, code, msg, null);
    }

    public static <T> R<T> failed(int code, String msg, String errMsg) {
        return restResult(null, code, msg, errMsg);
    }

    private static <T> R<T> restResult(T data, int code, String msg, String errMsg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setErrMsg(errMsg);
        apiResult.setTimestamp(System.currentTimeMillis());
        return apiResult;
    }

}
