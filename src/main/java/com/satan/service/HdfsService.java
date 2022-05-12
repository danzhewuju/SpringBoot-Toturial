package com.satan.service;

import com.satan.entity.RandomCopySingleBucketDataDo;
import com.satan.entity.CreateBucketDirectoriesDo;
import com.satan.entity.DelBucketsDataDo;
import com.satan.entity.UploadDataToMultiBucketsDo;

public interface HdfsService {


  //  String uploadFlinkBucket(UploadBucketDo uploadBucketDo);


  String createMultiBucketDirectories(CreateBucketDirectoriesDo createBucketDirectoriesDo) throws Exception;

  String deleteMultiBucketDirectories(DelBucketsDataDo delBucketsDataDo) throws Exception;


  void copySingleBucketDataToBase(RandomCopySingleBucketDataDo randomCopySingleBucketDataDo) throws Exception;

  void uploadFlinkToMultiBuckets(UploadDataToMultiBucketsDo uploadDataToMultiBucketsDo) throws Exception;
}
