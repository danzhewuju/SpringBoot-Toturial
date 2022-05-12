package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yuhao04
 * @date 2022/5/12 17:40
 */
@Data
public class DeployHotFixReleaseDo {
    @NotBlank(message = "flink version is null")
    private String sourceFlinkVersion; // CI/CD data has been upload this dir. (eg. FLINK-1.11.3.56)
    @NotBlank(message = "flink version is null")
    private String flinkVersion;  // eg. 1.11.3
    @NotBlank(message = "hot fix version is null")
    private String targetFlinkVersion; // eg. 1.11.3.40
}
