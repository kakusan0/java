package com.jp.login.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class mainRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Long main_role_id;
    private String main_role_name;
}
