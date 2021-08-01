package com.beyond.noteserver.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReadInModel  extends BaseInModel{
    private String name;
}
