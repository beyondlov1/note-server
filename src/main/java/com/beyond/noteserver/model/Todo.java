package com.beyond.noteserver.model;

import lombok.Data;

@Data
public class Todo {
    private String id;
    private Long remindTime;
    private boolean reminded;
}
