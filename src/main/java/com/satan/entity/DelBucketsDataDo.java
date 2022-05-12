package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class DelBucketsDataDo {
    @NotBlank(message = "flink version is blank")
    private String flinkVersion;
    private List<String> bucketIDs;
}
