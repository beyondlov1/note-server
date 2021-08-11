package com.beyond.noteserver.model;

import lombok.Data;

@Data
public class TodoReplaceUnit {
    private String repoAbsPath;
    private String filePath;
    private String content;

    public static TodoReplaceUnit of(String repoAbsPath, String filePath, String content){
        TodoReplaceUnit unit = new TodoReplaceUnit();
        unit.setRepoAbsPath(repoAbsPath);
        unit.setFilePath(filePath);
        unit.setContent(content);
        return unit;
    }
}
