package com.qibaili.warden.web.system.common.exception;

import com.qibaili.warden.web.system.common.bean.ResponseCode;
import lombok.*;

import java.io.Serializable;

/**
 * @author qibaili
 * @date 2018/11/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -5830606101342250630L;

    private Integer status;
    private String message;
    private Exception e;

    public RequestException(ResponseCode statusEnum, Exception e) {
        this.status = statusEnum.code;
        this.message = statusEnum.message;
        this.e = e;
    }

    public RequestException(ResponseCode statusEnum) {
        this.status = statusEnum.code;
        this.message = statusEnum.message;
    }

    public synchronized static RequestException fail(String message) {
        return RequestException.builder()
                .status(ResponseCode.FAIL.code)
                .message(message)
                .build();
    }

    public synchronized static RequestException fail(String message, Exception e) {
        return RequestException.builder()
                .status(ResponseCode.FAIL.code)
                .message(message)
                .e(e)
                .build();
    }

    public synchronized static RequestException fail(Integer code, String message, Exception e) {
        return RequestException.builder()
                .status(code)
                .message(message)
                .e(e)
                .build();
    }
}
