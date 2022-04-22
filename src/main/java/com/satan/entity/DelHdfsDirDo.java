package com.satan.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DelHdfsDirDo {
  @NotBlank(message = "job id is blank")
  private String jobId;
}
