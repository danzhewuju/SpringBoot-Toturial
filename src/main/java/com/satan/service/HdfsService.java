package com.satan.service;

import com.satan.entity.CopyDataToMultiBucketDo;
import com.satan.entity.CreateDirDo;
import com.satan.entity.DelHdfsDirDo;
import com.satan.entity.UploadBucketDo;

public interface HdfsService {
  String createHdfsDir(CreateDirDo createDirDo) throws Exception;

  String delHdfsDir(DelHdfsDirDo delHdfsDirDo) throws Exception;

//  String uploadFlinkBucket(UploadBucketDo uploadBucketDo);

  String copyHDFSToMultiBucket(CopyDataToMultiBucketDo copyDataToMultiBucketDo);
}
