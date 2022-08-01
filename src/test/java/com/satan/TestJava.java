package com.satan;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.satan.mode.User;
import org.apache.flink.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestJava {
    public static void main(String[] args) throws IOException {

        String path = "src/main/resources/test.json";
        File file = new File(path);
        String content = FileUtils.readFile(file, "utf-8");
        JsonArray jsonArray = new Gson().fromJson(content, JsonArray.class);
        List<User> result = new ArrayList<>();
        jsonArray.forEach(
                object -> {
                    result.add(new Gson().fromJson(object, User.class));
                });
        result.forEach(System.out::println);
    }
}
