package com.bigdata.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class HdfsDemo {

    Configuration configuration = null;
    FileSystem fs = null;

    @Before
    public void init() throws IOException {
        configuration = new Configuration(true);
        System.setProperty("HADOOP_USER_NAME", "root");
        fs = FileSystem.newInstance(configuration);
    }

    @Test
    public void mkdir() throws IOException {
        Path dir = new Path("/testHDFS");
        if(fs.exists(dir)){
            fs.delete(dir,true);
        }
        fs.mkdirs(dir);
    }

    @Test
    public void upload() throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("./data/data.txt")));
        Path out = new Path("/testHDFS/out.txt");
        FSDataOutputStream outputStream = fs.create(out);
        IOUtils.copyBytes(bis, outputStream, configuration, true);
    }

    @After
    public void destory() throws IOException {
        fs.close();
    }

}
