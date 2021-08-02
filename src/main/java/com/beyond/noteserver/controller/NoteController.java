package com.beyond.noteserver.controller;

import com.beyond.jgit.GitLite;
import com.beyond.jgit.GitLiteConfig;
import com.beyond.jgit.util.FileUtil;
import com.beyond.jgit.util.JsonUtils;
import com.beyond.jgit.util.PathUtils;
import com.beyond.noteserver.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class NoteController implements NoteControllerApi {


    private static final File CURRENT_REPO_FILE = new File("current_repo");
    private static final File RUNNING_REPOS_FILE = new File("running_repos");


    @Override
    public JsonResult<String> write(WriteInModel writeInModel) throws IOException {
        FileUtils.writeStringToFile(new File(PathUtils.concat(writeInModel.getRepoAbsPath(), writeInModel.getName())), writeInModel.getContent(), StandardCharsets.UTF_8);
        getOrCreateRepo(writeInModel.getRepoAbsPath());
        return JsonResult.success("success");
    }

    @Override
    public JsonResult<String> read(ReadInModel readInModel) throws IOException {
        return JsonResult.success(FileUtils.readFileToString(new File(PathUtils.concat(readInModel.getRepoAbsPath(), readInModel.getName())), StandardCharsets.UTF_8));
    }

    @Override
    public JsonResult<String> delete(DeleteInModel deleteInModel) throws IOException {
        FileUtils.deleteQuietly(new File(PathUtils.concat(deleteInModel.getRepoAbsPath(), deleteInModel.getName())));
        return JsonResult.success("success");
    }

    @Override
    public JsonResult<List<ListOutModel>> list(ListInModel listInModel) throws IOException {
        getOrCreateRepo(listInModel.getRepoAbsPath());
        Collection<File> files = FileUtil.listChildFilesAndDirs(listInModel.getRepoAbsPath(), x -> !x.getName().startsWith(".")).stream().filter(File::isFile).collect(Collectors.toList());
        return JsonResult.success(files.stream().map(x -> {
            ListOutModel outModel = new ListOutModel();
            String name = x.getAbsolutePath().replaceFirst(listInModel.getRepoAbsPath(), "");
            if (name.startsWith(File.separator)) {
                name = name.substring(1);
            }
            outModel.setName(name);
            outModel.setRepoAbsPath(listInModel.getRepoAbsPath());
            outModel.setType(FileType.FILE);
            return outModel;
        }).collect(Collectors.toList()));
    }

    @Override
    public JsonResult<List<ListReposOutModel>> listRepos(ListReposInModel inModel) throws IOException {
        List<ListReposOutModel> result = new ArrayList<>();
        Set<String> runningRepoAbsPaths = getRunningRepoAbsPaths();
        String current = getCurrentRepoAbsPath();
        for (String runningRepoAbsPath : runningRepoAbsPaths) {
            ListReposOutModel listReposOutModel = new ListReposOutModel();
            listReposOutModel.setCurrent(StringUtils.equals(runningRepoAbsPath, current));
            listReposOutModel.setRepoAbsPath(runningRepoAbsPath);
            GitLiteConfig config = GitLiteConfig.readFrom(runningRepoAbsPath);
            if (config != null) {
                listReposOutModel.setCommitterName(config.getCommitterName());
                listReposOutModel.setCommitterEmail(config.getCommitterEmail());
                if (CollectionUtils.isNotEmpty(config.getRemoteConfigs())){
                    GitLiteConfig.RemoteConfig remoteConfig = config.getRemoteConfigs().get(config.getRemoteConfigs().size() - 1);
                    listReposOutModel.setRemoteName(remoteConfig.getRemoteName());
                    listReposOutModel.setRemoteUrl(remoteConfig.getRemoteUrl());
                    listReposOutModel.setRemoteUserName(remoteConfig.getRemoteUserName());
                }
            }
            result.add(listReposOutModel);
        }
        return JsonResult.success(result);
    }

    @Override
    public JsonResult<String> upsertRepo(CreateRepoInModel inModel) throws IOException {
        GitLite git;
        if (StringUtils.isBlank(inModel.getCommitterName()) || StringUtils.isBlank(inModel.getCommitterEmail())) {
            git = getOrCreateRepo(inModel.getRepoAbsPath());
        } else {
            git = getOrCreateRepo(inModel.getRepoAbsPath(), inModel.getCommitterName(), inModel.getCommitterEmail());
        }
        if (StringUtils.isNotBlank(inModel.getRemoteName()) && StringUtils.isNotBlank(inModel.getRemoteUrl())) {
            GitLiteConfig config = git.getConfig();
            config.upsertRemote(inModel.getRemoteName(), inModel.getRemoteUrl(), inModel.getRemoteUserName(), inModel.getRemotePassword());
            config.save();
        }
        return JsonResult.success("success");
    }

    @Override
    public JsonResult<String> currentRepo() throws IOException {
        return JsonResult.success(getCurrentRepoAbsPath());
    }

    @Override
    public JsonResult<String> selectRepo(SelectRepoInModel inModel) throws IOException {
        FileUtils.write(CURRENT_REPO_FILE, inModel.getSelectedRepoAbsPath(), StandardCharsets.UTF_8);
        return JsonResult.success("success");
    }

    @Override
    public JsonResult<String> stopRunRepo(StopRunRepoInModel inModel) throws IOException {
        Set<String> runningRepos = getRunningRepoAbsPaths();
        runningRepos.remove(inModel.getSelectedRepoAbsPath());
        JsonUtils.writeTo(runningRepos, RUNNING_REPOS_FILE);
        return JsonResult.success("success");
    }

    private String getCurrentRepoAbsPath() throws IOException {
        if (CURRENT_REPO_FILE.exists()) {
            return FileUtils.readFileToString(CURRENT_REPO_FILE, StandardCharsets.UTF_8);
        }
        return null;
    }

    @SuppressWarnings("UnusedReturnValue")
    private GitLite getOrCreateRepo(String repoAbsPath, String committerName, String committerEmail) throws IOException {
        if (StringUtils.isBlank(repoAbsPath)) {
            throw new RuntimeException("repoAbsPath is null");
        }
        GitLite git;
        if (new File(PathUtils.concat(repoAbsPath, ".git", "config.json")).exists()) {
            GitLiteConfig config = GitLiteConfig.readFrom(repoAbsPath);
            if (StringUtils.isNotBlank(committerName) && StringUtils.isNotBlank(committerEmail)) {
                config.setCommitterName(committerName);
                config.setCommitterEmail(committerEmail);
                config.save();
            }
            git = config.build();
        } else {
            git = GitLiteConfig.simpleConfig(repoAbsPath, committerName, committerEmail)
                    .save()
                    .build();
            git.init();
        }

        Set<String> runningRepos = getRunningRepoAbsPaths();
        runningRepos.add(repoAbsPath);
        JsonUtils.writeTo(runningRepos, RUNNING_REPOS_FILE);
        return git;
    }

    @SuppressWarnings("UnusedReturnValue")
    private GitLite getOrCreateRepo(String repoAbsPath) throws IOException {
        return getOrCreateRepo(repoAbsPath, "beyond", "beyond@note.com");
    }

    private Set<String> getRunningRepoAbsPaths() throws IOException {
        if (RUNNING_REPOS_FILE.exists()) {
            String runningReposStr = FileUtils.readFileToString(RUNNING_REPOS_FILE, StandardCharsets.UTF_8);
            return JsonUtils.readValue(runningReposStr, new TypeReference<HashSet<String>>() {
            });
        } else {
            return new HashSet<>();
        }
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000,initialDelay = 1000)
    public void autoSync() throws IOException {
        Set<String> runningRepoAbsPaths = getRunningRepoAbsPaths();
        for (String runningRepoAbsPath : runningRepoAbsPaths) {
            GitLite git = getOrCreateRepo(runningRepoAbsPath);
            GitLiteConfig config = git.getConfig();
            List<GitLiteConfig.RemoteConfig> remoteConfigs = config.getRemoteConfigs();
            if (CollectionUtils.isNotEmpty(remoteConfigs)) {
                for (GitLiteConfig.RemoteConfig remoteConfig : remoteConfigs) {
                    String remoteName = remoteConfig.getRemoteName();
                    git.init();
                    git.add();
                    git.commit("auto commit");
                    log.info("{} sync with remote {}@{} start", runningRepoAbsPath, remoteName, remoteConfig.getRemoteUrl());
                    git.fetch(remoteName);
                    git.merge(remoteName);
                    git.packAndPush(remoteName);
                    log.info("{} sync with remote {}@{} end", runningRepoAbsPath, remoteName, remoteConfig.getRemoteUrl());
                }
            }
        }
    }
}
