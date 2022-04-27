package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CopyDataToMultiBucketDo {
    @NotBlank(message = "source tag is null")
    private String sourceTag;
    @NotBlank(message = "target tag is null")
    private String targetTag;
    @NotBlank(message = "SourceBucketID is null")
    private String SourceBucketID;
    @NotBlank(message = "TargetBucketID is null")
    private List<String> TargetBucketIDs;
}
