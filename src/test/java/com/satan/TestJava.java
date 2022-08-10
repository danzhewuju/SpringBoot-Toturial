package com.satan;

import com.satan.zookeeper.BaseZookeeper;

import java.io.IOException;

public class TestJava {
    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
                BaseZookeeper baseZookeeper = new BaseZookeeper();
                try {
                    baseZookeeper.connectZookeeper("localhost:2181");
                    baseZookeeper.createNode("/test03", "rewrite data test!");
                    baseZookeeper.closeConnection();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

    }
}
