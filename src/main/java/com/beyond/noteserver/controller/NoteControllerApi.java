package com.beyond.noteserver.controller;

import com.beyond.noteserver.model.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@RequestMapping("note-server")
public interface NoteControllerApi {

    @RequestMapping("/write")
    JsonResult<String> write(@RequestBody WriteInModel writeInModel) throws IOException ;

    @RequestMapping("/read")
    JsonResult<String> read(@RequestBody ReadInModel readInModel) throws  IOException ;

    @RequestMapping("/delete")
    JsonResult<String> delete(@RequestBody DeleteInModel readModel) throws  IOException ;

    @RequestMapping("/list")
    JsonResult<List<ListOutModel>> list(@RequestBody ListInModel listInModel) throws IOException;

    @RequestMapping("/listRepos")
    JsonResult<List<ListReposOutModel>> listRepos(@RequestBody ListReposInModel inModel) throws IOException;

    @RequestMapping("/upsertRepo")
    JsonResult<String> upsertRepo(@RequestBody CreateRepoInModel inModel) throws IOException;

    @RequestMapping("/currentRepo")
    JsonResult<String> currentRepo() throws IOException;

    @RequestMapping("/selectRepo")
    JsonResult<String> selectRepo(@RequestBody SelectRepoInModel inModel) throws IOException;

    @RequestMapping("/stopRunRepo")
    JsonResult<String> stopRunRepo(@RequestBody StopRunRepoInModel inModel) throws IOException;

}
