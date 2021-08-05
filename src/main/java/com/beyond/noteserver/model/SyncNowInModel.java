package com.beyond.noteserver.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SyncNowInModel extends BaseInModel {
    /**
     * null -> sync all
     */
    private String remoteName;
}
