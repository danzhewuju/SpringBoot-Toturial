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
}
