package com.satan.utils;

public enum YarnClusterType {
    JSSZ_K8S_YARN("7"),
    JSSZ_YARN("3"),
    JSCS_YARN("5");
    private String type;

    YarnClusterType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


}
