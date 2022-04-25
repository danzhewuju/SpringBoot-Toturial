package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CopyDataToMultiBucketDo {
    @NotBlank(message = "tag is null")
    private String tag;
    @NotBlank(message = "SourceBucketID is null")
    private String SourceBucketID;
    @NotBlank(message = "TargetBucketID is null")
    private List<String> TargetBucketIDs;
}
