package com.qibaili.warden.web.system.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qibaili
 * @date 2018/6/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "请求结果响应体")
public class ResponseResult<T> {


    @ApiModelProperty(value = "响应状态回执码")
    private Integer status;

    @ApiModelProperty(value = "数据体")
    private T data;

    @ApiModelProperty(value = "响应回执消息")
    private String message;

    @ApiModelProperty(value = "成功标识")
    private Boolean success;

    @ApiModelProperty(value = "响应时间戳")
    private final long time = System.currentTimeMillis();

    public synchronized static <T> ResponseResult<T> e(ResponseCode statusEnum, boolean success) {
        return e(statusEnum, success, null);
    }

    public synchronized static <T> ResponseResult<T> e(ResponseCode statusEnum, boolean success, T data) {
        ResponseResult<T> res = new ResponseResult<>();
        res.setStatus(statusEnum.code);
        res.setMessage(statusEnum.message);
        res.setData(data);
        res.setSuccess(success);
        return res;
    }
}
