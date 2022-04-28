package com.satan.service;

import com.satan.entity.RandomCopySingleBucketDataDo;
import com.satan.entity.CreateBucketDirectoriesDo;
import com.satan.entity.DelBucketsDataDo;

public interface HdfsService {


  //  String uploadFlinkBucket(UploadBucketDo uploadBucketDo);


  String createMultiBucketDirectories(CreateBucketDirectoriesDo createBucketDirectoriesDo) throws Exception;

  String deleteMultiBucketDirectories(DelBucketsDataDo delBucketsDataDo) throws Exception;

//  String uploadFlinkMultiBucket(UploadDataToMultiBucketsDo uploadDataToMultiBucketsDo) throws Exception;

  String copySingleBucketDataToBase(RandomCopySingleBucketDataDo randomCopySingleBucketDataDo) throws Exception;
}
