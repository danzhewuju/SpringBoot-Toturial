package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UploadDataToMultiBucketsDo {
    @NotBlank(message = "flink version is null")
    private String sourceFlinkVersion;                // CI/CD data has been upload this dir. eg. 1.13.56
    @NotBlank(message = "sourceBucket is null") // CI/CD data will be uploaded this bucket. eg. 1.13
    private String targetFlinkVersion;
    @NotEmpty(message = "targetBucket is null") // CI/CD data will be copied to these buckets. eg. bucket-1, bucket-2
    private List<String> targetBucketIDs;
}
