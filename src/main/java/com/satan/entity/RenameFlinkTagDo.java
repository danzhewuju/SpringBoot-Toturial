package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yuhao04
 * @date 2022/5/16 14:33
 */
@Data
public class RenameFlinkTagDo {
    @NotBlank(message = "originalTagName is null")
    private String originalTagName;
    @NotBlank(message = "newTagName is null")
    private String newTagName;
}
