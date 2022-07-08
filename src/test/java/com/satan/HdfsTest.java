package com.satan;

import com.satan.service.HdfsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest()
public class HdfsTest {
    @Autowired
    HdfsService hdfsService;

    private String hdfsPath = "hdfs://feifish.site:9000";

    @Test
    public void getStatusTest() throws IOException, URISyntaxException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("dfs.client.use.datanode.hostname", "true");
        FileSystem fs = FileSystem.get(new URI(hdfsPath), conf, "ubuntu");
        FileStatus[] fileStatuses = fs.listStatus(new Path("/test"));
        for (FileStatus fileStatus : fileStatuses) {
            log.info(fileStatus.getPath().getName());
        }
    }

    @Test
    public void testDelCheckpoint() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("dfs.client.use.datanode.hostname", "true");
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
        Arrays.stream(fileStatuses).map(path -> path.getPath().getName()).
                filter(name -> name.startsWith("chk-") && Integer.parseInt(name.substring(4))
                        < Integer.parseInt(delChk.substring(4))).forEach(name -> {
                    String path = "/test/test-checkpoint/" + name;
                    try {
                        fs.delete(new Path(path), true);
                        log.info("delete {}", path);
                    } catch (IOException e) {
                        log.error("delete hdfs file failed, path: {}", path);
                    }
                });

    }
}
