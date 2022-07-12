package com.satan;


import lombok.extern.slf4j.Slf4j;
import org.apache.flink.runtime.checkpoint.Checkpoints;
import org.apache.flink.runtime.checkpoint.OperatorSubtaskState;
import org.apache.flink.runtime.checkpoint.metadata.CheckpointMetadata;
import org.apache.flink.runtime.state.IncrementalRemoteKeyedStateHandle;
import org.apache.flink.runtime.state.KeyedStateHandle;
import org.apache.flink.runtime.state.filesystem.FileStateHandle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
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
    public static Set<String> findDependentFilesByCheckpointMeta(CheckpointMetadata checkpointMetadata) {

        return checkpointMetadata.getOperatorStates().stream()
                .map(operatorState -> operatorState.getSubtaskStates())
                .flatMap((Function<Map<Integer, OperatorSubtaskState>, Stream<OperatorSubtaskState>>) subtaskIndex2OperatorSubtaskState -> subtaskIndex2OperatorSubtaskState.values().stream()).flatMap((Function<OperatorSubtaskState, Stream<KeyedStateHandle>>) operatorSubtaskState -> {
                    Set<KeyedStateHandle> allKeyedStateHandles = new HashSet<>();
                    allKeyedStateHandles.addAll(operatorSubtaskState.getManagedKeyedState());
                    allKeyedStateHandles.addAll(operatorSubtaskState.getRawKeyedState());
                    return allKeyedStateHandles.stream();
                })
                .filter(keyedStateHandle -> keyedStateHandle instanceof IncrementalRemoteKeyedStateHandle).flatMap(incrementalRemoteKeyedStateHandle -> ((IncrementalRemoteKeyedStateHandle) incrementalRemoteKeyedStateHandle).getSharedState().values().stream())
                .filter(keyedStateHandle -> keyedStateHandle instanceof FileStateHandle)
                .map(fileStateHandle -> ((FileStateHandle) fileStateHandle).getFilePath().toString()).collect(Collectors.toSet());

    }

    @Test
    public void readMetaData() throws IOException {
        String path = "src/main/resources/_metadata";
        FileInputStream input = new FileInputStream(path);
        BufferedInputStream bufferedInput = new BufferedInputStream(input);
        DataInputStream dataInputStream = new DataInputStream(bufferedInput);
        CheckpointMetadata checkpointMetadata = Checkpoints.loadCheckpointMetadata(dataInputStream, ParseMetaDataTest.class.getClassLoader(), null);
        log.info("checkpointMetadata ID: {}", checkpointMetadata.getCheckpointId());
        log.info("checkpointMetadata Master States: {}", checkpointMetadata.getMasterStates());
        log.info("checkpointMetadata Operator States: {}", checkpointMetadata.getOperatorStates());
        Set<String> dependentFilesByCheckpointMeta = ParseMetaDataTest.findDependentFilesByCheckpointMeta(checkpointMetadata);
        log.info("Finished!!!");

    }

    @Test
    public void testDelChk() {

        // 过滤出列表中小于chk-150的字符串
//        Arrays.stream(res).filter(s -> s.startsWith("chk-") && Integer.parseInt(s.substring(4)) < 150).forEach(System.out::println);
//        Arrays.stream(res).filter(path -> path.contains("chk")).filter(Function<>).forEach(System.out::println);
    }

}
