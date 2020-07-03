package com.cnnc.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    public static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Long age;

}
