package com.beyond.noteserver.model;

import com.beyond.noteserver.annotaion.TrimDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
public class CreateRepoInModel {
    @JsonDeserialize(using = TrimDeserializer.class)
    private String repoAbsPath;
    @JsonDeserialize(using = TrimDeserializer.class)
    private String committerName;
    @JsonDeserialize(using = TrimDeserializer.class)
    private String committerEmail;
    @JsonDeserialize(using = TrimDeserializer.class)
    private String remoteName;
    @JsonDeserialize(using = TrimDeserializer.class)
    private String remoteUrl;
    @JsonDeserialize(using = TrimDeserializer.class)
    private String remoteUserName;
    @JsonDeserialize(using = TrimDeserializer.class)
    private String remotePassword;
}
