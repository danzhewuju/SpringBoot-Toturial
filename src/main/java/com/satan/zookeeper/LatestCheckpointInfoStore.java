package com.satan.zookeeper;

import lombok.Data;

import java.io.Serializable;

/**
 * This class is used to store the latest checkpoint information, and this information will be
 * written into zk or other persistent storage every time a checkpoint occurs.
 */
@Data
public class LatestCheckpointInfoStore implements Serializable {

    private static final long serialVersionUID = 1L;
    private String checkpointPath;
    private long triggerCheckpointTimestampMillis;
    private long checkpointBaseIntervalSeconds;

    public LatestCheckpointInfoStore(String checkpointPath, long triggerCheckpointTimestampMillis, long checkpointBaseIntervalSeconds) {
        this.checkpointPath = checkpointPath;
        this.checkpointBaseIntervalSeconds = checkpointBaseIntervalSeconds;
        this.triggerCheckpointTimestampMillis = triggerCheckpointTimestampMillis;
    }

    public String getCheckpointPath() {
        return checkpointPath;
    }
}
