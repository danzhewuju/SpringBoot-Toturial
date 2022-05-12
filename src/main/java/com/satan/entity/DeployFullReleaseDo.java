package com.satan.entity;

import lombok.Data;

@Data
public class DeployFullReleaseDo {
    private RandomCopySingleBucketDataDo randomCopySingleBucketDataDo;
    private DelBucketsDataDo delBucketsDataDo;
}
