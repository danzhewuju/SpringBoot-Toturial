package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UploadDataToMultiBucketsDo {
    @NotBlank(message = "flink version is null")
    private String sourceFlinkVersion; // CI/CD data has been upload this dir. (eg. FLINK-1.11.3.56-1.11.3.50)

    @NotBlank(message = "sourceBucket is null")
    private String targetFlinkVersion; // CI/CD data will be uploaded this bucket. (eg. 1.11.3)

    @NotEmpty(message = "targetBucket is null")
    private List<String>
            targetBucketIDs; // CI/CD data will be copied to these buckets. (eg. bucket-1, bucket-2)
}
