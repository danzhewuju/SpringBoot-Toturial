package com.satan;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.runtime.checkpoint.Checkpoints;
import org.apache.flink.runtime.checkpoint.OperatorState;
import org.apache.flink.runtime.checkpoint.OperatorSubtaskState;
import org.apache.flink.runtime.checkpoint.metadata.CheckpointMetadata;
import org.apache.flink.runtime.state.IncrementalRemoteKeyedStateHandle;
import org.apache.flink.runtime.state.KeyedStateHandle;
import org.apache.flink.runtime.state.TaishanKeyedStateHandle;
import org.apache.flink.runtime.state.filesystem.FileStateHandle;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest()
public class ParseMetaDataTest {

    private final String hdfsPath = "hdfs://localhost:9000";
    private static final String USER = "alex";

    public static Set<String> findDependentFilesByCheckpointMeta(CheckpointMetadata checkpointMetadata) {

        return checkpointMetadata.getOperatorStates().stream().map(operatorState -> operatorState.getSubtaskStates())
                                 .flatMap(
                                         (Function<Map<Integer, OperatorSubtaskState>, Stream<OperatorSubtaskState>>) subtaskIndex2OperatorSubtaskState -> subtaskIndex2OperatorSubtaskState.values()
                                                                                                                                                                                            .stream())
                                 .flatMap(
                                         (Function<OperatorSubtaskState, Stream<KeyedStateHandle>>) operatorSubtaskState -> {
                                             Set<KeyedStateHandle> allKeyedStateHandles = new HashSet<>();
                                             allKeyedStateHandles.addAll(operatorSubtaskState.getManagedKeyedState());
                                             allKeyedStateHandles.addAll(operatorSubtaskState.getRawKeyedState());
                                             return allKeyedStateHandles.stream();
                                         })
                                 .filter(keyedStateHandle -> keyedStateHandle instanceof IncrementalRemoteKeyedStateHandle)
                                 .flatMap(
                                         incrementalRemoteKeyedStateHandle -> ((IncrementalRemoteKeyedStateHandle) incrementalRemoteKeyedStateHandle).getSharedState()
                                                                                                                                                     .values()
                                                                                                                                                     .stream())
                                 .filter(keyedStateHandle -> keyedStateHandle instanceof FileStateHandle)
                                 .map(fileStateHandle -> ((FileStateHandle) fileStateHandle).getFilePath().toString())
                                 .collect(Collectors.toSet());

    }

    @Test
    public void readMetaData() throws IOException {
        String path = "src/main/resources/_metadata";
        FileInputStream input = new FileInputStream(path);
        BufferedInputStream bufferedInput = new BufferedInputStream(input);
        DataInputStream dataInputStream = new DataInputStream(bufferedInput);
        CheckpointMetadata checkpointMetadata = Checkpoints.loadCheckpointMetadata(dataInputStream,
                                                                                   ParseMetaDataTest.class.getClassLoader(),
                                                                                   null);
        log.info("checkpointMetadata ID: {}", checkpointMetadata.getCheckpointId());
        log.info("checkpointMetadata Master States: {}", checkpointMetadata.getMasterStates());
        log.info("checkpointMetadata Operator States: {}", checkpointMetadata.getOperatorStates());
        Set<String> dependentFilesByCheckpointMeta = ParseMetaDataTest.findDependentFilesByCheckpointMeta(
                checkpointMetadata);

        String taiShanTable = null;
        if (StringUtils.isEmpty(taiShanTable)) {
            System.out.println("false");
        }
        for (OperatorState operatorState : checkpointMetadata.getOperatorStates()) {
            for (OperatorSubtaskState operatorSubtaskState : operatorState.getSubtaskStates().values()) {
                for (KeyedStateHandle keyedStateHandle : operatorSubtaskState.getManagedKeyedState()) {
                    if (keyedStateHandle instanceof TaishanKeyedStateHandle) {
                        String taishanTableName = ((TaishanKeyedStateHandle) keyedStateHandle).getTaishanTableName();
                        if (!StringUtils.isEmpty(taishanTableName)) {
                            taiShanTable = taishanTableName;

                        }
                    }
                }
            }
        }
        log.info(taiShanTable);

    }

    @Test
    public void testDelChk() throws URISyntaxException, IOException, InterruptedException {

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(hdfsPath), conf, USER);

        FSDataInputStream fsDataInputStream = fs.open(new Path("/flink/checkpoints/_metadata"));
        CheckpointMetadata checkpointMetadata = Checkpoints.loadCheckpointMetadata(fsDataInputStream,
                                                                                   this.getClass().getClassLoader(),
                                                                                   null);
        for (OperatorState operatorState : checkpointMetadata.getOperatorStates()) {
            for (OperatorSubtaskState operatorSubtaskState : operatorState.getSubtaskStates().values()) {
                for (KeyedStateHandle keyedStateHandle : operatorSubtaskState.getManagedKeyedState()) {
                    if (keyedStateHandle instanceof TaishanKeyedStateHandle) {
                        String tableName = ((TaishanKeyedStateHandle) keyedStateHandle).getTaishanTableName();
                        if (!StringUtils.isEmpty(tableName)) {
                            System.out.println(tableName);
                        }
                    }
                }
            }
        }
    }

}
