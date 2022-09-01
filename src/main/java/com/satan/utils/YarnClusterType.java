package com.satan.utils;

import lombok.Getter;

public enum YarnClusterType {

    YARN_JSSZ_K8S("7"),

    YARN_JSSZ("3"),

    YARN_JSCS("5"),

    YARN_NOT_EXIST("-1");

    @Getter
    private String clusterId;

    YarnClusterType(String clusterId) {
        this.clusterId = clusterId;
    }

    public static YarnClusterType findTypeByClusterId(String clusterId){
        for (YarnClusterType yarnClusterType:YarnClusterType.values()){
            if(yarnClusterType.getClusterId().equals(clusterId)){
                return yarnClusterType;
            }
        }
        return YarnClusterType.YARN_NOT_EXIST;
    }

    public static void main(String[] args) {
        YarnClusterType yarnClusterType = YarnClusterType.findTypeByClusterId("7");
        switch (yarnClusterType){
            case YARN_JSCS:
                System.out.println("JSCS");
                break;
            case YARN_JSSZ_K8S:
                System.out.println("JSSZ_K8S");
                break;
            case YARN_JSSZ:
                System.out.println("JSSZ");
                break;
        }
    }
}

