package com.satan;

import com.satan.service.HdfsService;
import com.satan.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.runtime.checkpoint.Checkpoints;
import org.apache.flink.runtime.checkpoint.metadata.CheckpointMetadata;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.htrace.shaded.commons.logging.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest()
public class HdfsTest {
    @Autowired
    HdfsService hdfsService;

    private final String hdfsPath = "hdfs://localhost:9000";
    private static final String USER = "alex";

    @Test
    public void getStatusTest() throws IOException, URISyntaxException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(hdfsPath), conf, USER);
        FileStatus[] fileStatuses = fs.listStatus(new Path("/flink"));
        for (FileStatus fileStatus : fileStatuses) {
            log.info(fileStatus.getPath().getName());
        }
    }

    @Test
    public void testDelCheckpoint() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(hdfsPath), conf, "ubuntu");
        FileSystem fs1 = FileSystem.get(new URI(hdfsPath), conf, "ubuntu");
        fs.mkdirs(new Path("/test/test-checkpoint"));
        fs.mkdirs(new Path("/test/test-checkpoint/chk-22"));
        fs.mkdirs(new Path("/test/test-checkpoint/chk-23"));
        fs.mkdirs(new Path("/test/test-checkpoint/chk-24"));
        fs.mkdirs(new Path("/test/test-checkpoint/chk-26"));
        fs.mkdirs(new Path("/test/test-checkpoint/chk-27"));
        FileStatus[] fileStatuses = fs.listStatus(new Path("/test/test-checkpoint"));
        String delChk = "chk-26";
        Arrays.stream(fileStatuses).map(path -> path.getPath().getName()).filter(name -> name.startsWith("chk-") && Integer.parseInt(name.substring(4)) < Integer.parseInt(delChk.substring(4))).forEach(name -> {
            String path = "/test/test-checkpoint/" + name;
            try {
                fs.delete(new Path(path), true);
                log.info("delete {}", path);
            } catch (IOException e) {
                log.error("delete hdfs file failed, path: {}", path);
            }
        });

    }

    @Test
    public void testMoveFile() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(hdfsPath), conf, USER);
        String source = "/tmp/flink";
        Path sourcePath = new Path(source);
        System.out.println(sourcePath);
        String target = source.replaceFirst("tmp", "tmp/yuhao04/trash");
        log.info("target;{}", target);
        Path targetPath = new Path(target);
        if(!fs.exists(targetPath.getParent())){
            log.info("创建父目录");
            fs.mkdirs(targetPath.getParent());
        }
        log.info("source: {}, target: {}", source, target);

        fs.rename(sourcePath, targetPath);
//        FileUtil.copy(fs, targetPath, fs,sourcePath, true, fs.getConf());

    }

    @Test
    public void testConenctPath() {
        String s = Constants.CHECKPOINT_HDFS_PATH.replaceAll("viewfs://jssz-bigdata-cluster", "");
        Path path = new Path(Constants.CHECKPOINT_HDFS_TEMPORARY_PATH + s);
        log.info(path.toString());
    }

    @Test
    public void testHdfsModifyTime() throws IOException, URISyntaxException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(hdfsPath), conf, USER);
        String source = "/tmp/yuhao04";
        fs.close();
        if (fs == null) {

        }
        Path sourcePath = new Path(source);
        FileStatus fileStatus = Arrays.stream(fs.listStatus(sourcePath.getParent()))
                                      .filter(f -> f.getPath().getName().equals(sourcePath.getName()))
                                      .collect(Collectors.toList()).get(0);

        log.info("name:{}, mtime:{}", fileStatus.getPath(), fileStatus.getModificationTime());

    }


    @Test
    public void testListMetaData() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        List<String> result = new ArrayList<>();
        String directoryPath = "/flink/saberID1";
        FileSystem fs = FileSystem.get(new URI(hdfsPath), conf, USER);
        List<FileStatus> jobInstanceStatuses = Arrays.asList(fs.listStatus(new Path(directoryPath)));
        jobInstanceStatuses.sort(Comparator.comparingLong(FileStatus::getModificationTime).reversed());
        for (FileStatus jobInstanceStatus : jobInstanceStatuses) {
            Arrays.stream(fs.listStatus(jobInstanceStatus.getPath()))
                  .filter(fileStatus -> fileStatus.getPath().getName().startsWith("chk-"))
                  .sorted(Comparator.comparingLong(FileStatus::getModificationTime).reversed()).forEach(fileStatus -> {
                      try {
                          for (FileStatus status : fs.listStatus(fileStatus.getPath())) {
                              if (status.getPath().getName().equals("_metadata")) {
                                  result.add(status.getPath().toString());
                                  break;
                              }
                          }
                      } catch (IOException e) {
                          throw new RuntimeException(e);
                      }
                  });
        }
        result.forEach(System.out::println);

    }

    @Test
    public void testReadMetadataFile() throws URISyntaxException, IOException, InterruptedException {
        FileSystem fs = FileSystem.get(new URI(hdfsPath), new Configuration(), USER);
        String path = "/data/_metadata";
        FSDataInputStream data = fs.open(new Path(path));
        CheckpointMetadata metadata = Checkpoints.loadCheckpointMetadata(data, this.getClass().getClassLoader(), null);
        log.info("metadata:{}", metadata);
    }
}
