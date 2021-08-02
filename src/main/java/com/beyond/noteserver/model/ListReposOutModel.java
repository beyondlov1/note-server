package com.beyond.noteserver.model;

import lombok.Data;

@Data
public class ListReposOutModel {

    private boolean current;
    private String repoAbsPath;
    private String committerName;
    private String committerEmail;
    private String remoteName;
    private String remoteUrl;
    private String remoteUserName;
}
