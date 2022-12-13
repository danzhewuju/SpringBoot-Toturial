package com.satan;

import com.bilibili.datatrace.client.tools.Tracer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestJava {
    public static void main(String[] args) {
      TestA testB = new TestB();
      if (testB instanceof TestA){
          System.out.println("yes");
          testB.getName();
      }else {
            System.out.println("no");
      }
    }
}
