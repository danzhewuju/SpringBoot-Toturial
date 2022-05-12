package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CreateBucketDirectoriesDo {
    @NotBlank(message = "flink version is blank")
    private String flinkVersion;
    @NotEmpty(message = "bucket id list is Empty")
    private List<String> bucketIDs;
}
