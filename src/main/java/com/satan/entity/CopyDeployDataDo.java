package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CopyDeployDataDo {
    @NotBlank(message = "source tag is null")
    private String sourceTag;
    @NotBlank(message = "target tag is null")
    private String targetTag;
}
