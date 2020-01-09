package com.qibaili.warden.web.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author qibaili
 * @date 2018-06-06
 */
@Entity
@Table(name = "SYSTEM_USER")
@Data
public class SystemUser {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String name;

    private String username;

    @JSONField(serialize = false)
    private String password;

    @JSONField(serialize = false)
    private String salt;

    private String email;

    private String telephone;

    /**
     * 1 启用
     * 0 禁用
     */
    private String status;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "CREATE_PERSON")
    private String createPerson;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    @Column(name = "UPDATE_PERSON")
    private String updatePerson;
}
