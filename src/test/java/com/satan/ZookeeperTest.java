package com.satan;

import com.google.gson.Gson;
import com.satan.zookeeper.BaseZookeeper;
import com.satan.zookeeper.LatestCheckpointInfoStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.List;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest()
public class ZookeeperTest {

    public static final String ZOOKEEPER_HOST = "localhost:2181";


    @Test
    public void ZookeeperConnectorTest() throws Exception {
        BaseZookeeper baseZookeeper = new BaseZookeeper();
        baseZookeeper.connectZookeeper(ZOOKEEPER_HOST);
        List<String> children = baseZookeeper.getChildren("/");
        System.out.println(children);
        baseZookeeper.closeConnection();
    }

    @Test
    public void ZookeeperWriteTest() throws Exception {
        BaseZookeeper baseZookeeper = new BaseZookeeper();
        baseZookeeper.connectZookeeper(ZOOKEEPER_HOST);
        baseZookeeper.createNode("/test01", "rewrite data test!");
        baseZookeeper.closeConnection();

//        ZookeeperConnectorTest();


    }

    @Test
    public void zookeeperReadConnector() throws Exception {
        BaseZookeeper baseZookeeper = new BaseZookeeper();
        baseZookeeper.connectZookeeper(ZOOKEEPER_HOST);
        String data = baseZookeeper.getData("/test");
        log.info("read zookeeper {} data : {}", "/test", data);
        baseZookeeper.closeConnection();


    }


    @Test
    public void zookeeperRewriteTest() throws Exception {
        BaseZookeeper baseZookeeper = new BaseZookeeper();
        baseZookeeper.connectZookeeper(ZOOKEEPER_HOST);
        baseZookeeper.setData("/test", "this is second data.");
        baseZookeeper.closeConnection();

    }

    @Test
    public void zookeeperDeletedTest() throws Exception {
        BaseZookeeper baseZookeeper = new BaseZookeeper();
        baseZookeeper.connectZookeeper(ZOOKEEPER_HOST);
        baseZookeeper.deleteNode("/test");
        baseZookeeper.closeConnection();

    }

    @Test
    public void readClassTest() throws Exception {
        BaseZookeeper baseZookeeper = new BaseZookeeper();
        baseZookeeper.connectZookeeper(ZOOKEEPER_HOST);
//        String data = baseZookeeper.getData("/flink/default/realtime/flink/latest_checkpoints");
//        log.info(data);
        byte[] data = baseZookeeper.zookeeper.getData("/flink/default/realtime/flink/latest_checkpoints", false, null);
        LatestCheckpointInfoStore latestCheckpointInfoStore = new Gson().fromJson(new String(data), LatestCheckpointInfoStore.class);
        // 毫秒时间戳转化为标准时间
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestCheckpointInfoStore.getTriggerCheckpointTimestampMillis());

        log.info(latestCheckpointInfoStore.getCheckpointPath());
        log.info("{}", date);
        log.info("{}", latestCheckpointInfoStore.getCheckpointBaseIntervalSeconds());


    }

    @Test
    public void multiThreadCreateNodeTest() {
//        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                log.info(Thread.currentThread().getName());
                BaseZookeeper baseZookeeper = new BaseZookeeper();
                try {
                    baseZookeeper.connectZookeeper("localhost:2181");
                    baseZookeeper.createNode("/test02", "rewrite data test!");
                    baseZookeeper.closeConnection();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }).start();

//        }
    }


}
