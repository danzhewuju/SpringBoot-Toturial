package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UploadDataToMultiBucketDo {
    @NotBlank(message = "tag is null")
    private String tag;
    @NotBlank(message = "sourceBucket is null") // CI/CD data will be uploaded this bucket.
    private String sourceBucketID;
    @NotBlank(message = "targetBucket is null") // CI/CD data will be copied to these buckets.
    private List<String> targetBucketIDs;
    @NotBlank(message = "boss path is blank") // CI/CD data directory.
    private String bossPath;
    @NotBlank(message = "file name is blank") // CI/CD data filename.
    private String fileName;

}
