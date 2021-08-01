package com.beyond.noteserver.model;

import com.beyond.jgit.GitLiteConfig;
import lombok.Data;

import java.io.IOException;

@Data
public class Repo {
    private boolean current;
    private String repoAbsPath;

    public static Repo of(String repoAbsPath) {
        Repo repo = new Repo();
        repo.setRepoAbsPath(repoAbsPath);
        return repo;
    }

    public GitLiteConfig readConfig() throws IOException {
        return GitLiteConfig.readFrom(repoAbsPath);
    }
}
