package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RandomCopySingleBucketDataDo {
    @NotBlank(message = "source flink version is null")
    private String sourceFlinkVersion;
    @NotBlank(message = "target flink version is null")
    private String targetFlinkVersion;
}
