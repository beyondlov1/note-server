package com.beyond.noteserver.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author beyond
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeleteInModel  extends BaseInModel{
    private String name;
}
