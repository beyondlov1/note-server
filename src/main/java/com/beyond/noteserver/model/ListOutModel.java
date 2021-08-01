package com.beyond.noteserver.model;

import lombok.Data;

@Data
public class ListOutModel {
    private String repoAbsPath;
    private String name;
    private FileType type;
}
