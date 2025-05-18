package com.jp.login.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MasterUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private user user;
    private role role;
}
