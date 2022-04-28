package com.satan.service;

import com.satan.entity.CopyDeployDataDo;
import com.satan.entity.CreateBucketDirectoriesDo;
import com.satan.entity.DelBucketsDataDo;

public interface HdfsService {


  //  String uploadFlinkBucket(UploadBucketDo uploadBucketDo);


  String createHdfsDir(CreateBucketDirectoriesDo createBucketDirectoriesDo) throws Exception;

  String delHdfsBucketDir(DelBucketsDataDo delBucketsDataDo) throws Exception;

//  String uploadFlinkMultiBucket(UploadDataToMultiBucketsDo uploadDataToMultiBucketsDo) throws Exception;

  String copyDeployData(CopyDeployDataDo copyDeployDataDo) throws Exception;
}
