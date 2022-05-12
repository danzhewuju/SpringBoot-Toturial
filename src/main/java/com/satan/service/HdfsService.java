package com.satan.service;

import com.satan.entity.*;

import java.io.IOException;

public interface HdfsService {


  //  String uploadFlinkBucket(UploadBucketDo uploadBucketDo);


  String createMultiBucketDirectories(CreateBucketDirectoriesDo createBucketDirectoriesDo) throws Exception;

  String deleteMultiBucketDirectories(DelBucketsDataDo delBucketsDataDo) throws Exception;

  void uploadFlinkToMultiBuckets(DeployGrayReleaseDo deployGrayReleaseDo) throws Exception;

  void copySingleBucketDataToBase(RandomCopySingleBucketDataDo randomCopySingleBucketDataDo) throws Exception;

  void deployHotfixRelease(DeployHotFixReleaseDo deployHotFixReleaseDo) throws Exception;

  String pollLastGrayDeployVersion(String flinkVersion) throws IOException;

  boolean setLastGrayDeployVersion(String flinkVersion, String grayDeployVersion) throws IOException;
}
