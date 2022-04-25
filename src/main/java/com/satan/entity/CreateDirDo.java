package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateDirDo {
    @NotBlank(message = "flink version is blank")
    private String tag;
    @NotBlank(message = "bucket id is null")
    private String bucketID;
}
