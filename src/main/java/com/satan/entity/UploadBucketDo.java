package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UploadBucketDo {
    @NotBlank(message = "flink version is blank")
    private String tag;
    @NotBlank(message = "bucket id is null")
    private String bucketID;
    @NotBlank(message = "boss path is blank")
    private String bossPath;
    @NotBlank(message = "file name is blank")
    private String fileName;

}
