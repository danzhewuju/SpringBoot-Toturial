package com.satan.service;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LoadFile {

    public void getPathFile() throws IOException {
        File metadata = new File("/Users/alex/Program/SpringBoot-Toturial/src/main/resources/yarn", "_metadata");
        if(metadata.exists()){
            System.out.println("文件存在！");
        }

    }
}
