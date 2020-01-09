package com.qibaili.warden.web.system.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author qibaili
 * @date 2018-05-28
 */
@Data
public class PageDTO {

    @NotNull
    private Integer page;

    @Range(max = 1000)
    private Integer pageSize;
}
