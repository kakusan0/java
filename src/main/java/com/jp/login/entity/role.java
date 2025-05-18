package com.jp.login.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private mainRole mainRole;
    private List<groupRole> groupRole;
}
