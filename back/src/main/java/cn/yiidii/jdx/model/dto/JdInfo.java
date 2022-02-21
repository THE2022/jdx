package cn.yiidii.jdx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * JdInfo
 *
 * @author ed w
 * @since 1.0
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class JdInfo {

    private String gsalt;
    private String guid;
    private String lsId;
    private String rsaModulus;

    private Long expireTime;

    private String preCookie;
    private String cookie;

}
