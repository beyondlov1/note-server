package com.beyond.noteserver.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WriteInModel extends BaseInModel {
    private String name;
    private String content;
}
