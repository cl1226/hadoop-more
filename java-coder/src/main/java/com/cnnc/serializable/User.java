package com.cnnc.serializable;

import lombok.Data;

import java.beans.Transient;
import java.io.Serializable;

@Data
public class User implements Serializable {

    public static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Long age;

    private transient String xx = "xxx";

    private int a;

}
