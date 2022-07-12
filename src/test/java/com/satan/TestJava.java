package com.satan;

import java.util.Arrays;

public class TestJava {
    public static void main(String[] args) {
        int [] a = {8, 43, 5, 6};
        Arrays.stream(a).sorted().toArray();
        System.out.println(Arrays.toString(a));

    }
}
