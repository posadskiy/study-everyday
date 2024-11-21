package com.posadskiy.spring.bpp.dto;

import lombok.Data;

@Data
public class ToDo {

    public Integer id;
    public String text;
    public Boolean isDone;

}
