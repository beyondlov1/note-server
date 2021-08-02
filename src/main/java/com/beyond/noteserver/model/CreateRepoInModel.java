package com.beyond.noteserver.model;

import lombok.Data;

@Data
public class CreateRepoInModel {
    private String repoAbsPath;
    private String committerName;
    private String committerEmail;
    private String remoteName;
    private String remoteUrl;
    private String remoteUserName;
    private String remotePassword;
}
