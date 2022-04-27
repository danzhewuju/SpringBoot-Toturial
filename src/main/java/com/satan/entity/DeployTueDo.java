package com.satan.entity;

import lombok.Data;

@Data
public class DeployTueDo {
    private CreateDirDo createDirDo; // create dirs
    private UploadDataToMultiBucketDo uploadDataToMultiBucketDo; // upload data to dir and copy date.
}
