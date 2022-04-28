package com.satan.entity;

import lombok.Data;

@Data
public class DeployOnTueDo {
    private CreateBucketDirectoriesDo createBucketDirectoriesDo; // create dirs
    private UploadDataToMultiBucketsDo uploadDataToMultiBucketsDo; // upload data to dir and copy date.
}
