package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CreateDirDo {
    @NotBlank(message = "flink version is blank")
    private String tag;
    @NotBlank(message = "bucket id is null")
    private List<String> bucketIDs;
}
