package cn.yiidii.jdx.model;

/**
 *  异常编码
 * 
 * @author YiiDii Wang
 * @date 2021/2/11 14:48:10
 */
public interface BaseExceptionCode {
    /**
     * 异常编码
     *
     * @return 异常编码
     */
    int getCode();

    /**
     * 异常消息
     *
     * @return 异常消息
     */
    String getMsg();
}
