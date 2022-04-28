package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CreateBucketDirectoriesDo {
    @NotBlank(message = "flink version is blank")
    private String flinkVersion;
    @NotBlank(message = "bucket id list is null")
    private List<String> bucketIDs;
}
