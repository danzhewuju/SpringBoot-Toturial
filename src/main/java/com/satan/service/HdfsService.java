package com.satan.service;

import com.satan.entity.CopyDeployDataDo;
import com.satan.entity.CreateDirDo;
import com.satan.entity.DelHdfsDirDo;

public interface HdfsService {
  String createHdfsDir(CreateDirDo createDirDo) throws Exception;

  String delHdfsBucketDir(DelHdfsDirDo delHdfsDirDo) throws Exception;

  //  String uploadFlinkBucket(UploadBucketDo uploadBucketDo);

  String copyDeployData(CopyDeployDataDo copyDeployDataDo) throws Exception;
}
