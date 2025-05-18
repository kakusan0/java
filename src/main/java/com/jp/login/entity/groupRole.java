package com.jp.login.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class groupRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Long main_role_id;
    private String group_role_id;
    private String group_role_name;
}
