package com.qibaili.warden.web.system.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author qibaili
 * @date 2018-08-14
 */
@Entity
@Table(name = "SYSTEM_LOG")
@Data
public class SystemLog {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String username;

    @Column(name = "USER_ID")
    private String userId;

    private String ip;

    private Integer ajax;

    private String uri;

    private String params;

    @Column(name = "HTTP_METHOD")
    private String httpMethod;


    @Column(name = "CLASS_METHOD")
    private String classMethod;

    private String action;

    @Column(name = "CREATE_TIME")
    private Date createTime;
}
