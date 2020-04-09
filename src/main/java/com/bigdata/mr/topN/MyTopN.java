package com.bigdata.mr.topN;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

public class MyTopN {

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration(true);

        GenericOptionsParser parser = new GenericOptionsParser(configuration, args);
        String[] otherargs = parser.getRemainingArgs();

        configuration.set("mapreduce.app-submission.cross-platform","true");
        configuration.set("mapreduce.framework.name", "local");

        Job job = Job.getInstance(configuration);
        job.setJar("E:\\hadoop\\hadoop-more\\target\\hadoop-more-1.0-SNAPSHOT.jar");
        job.setJarByClass(MyTopN.class);

//        TextInputFormat
    }

}
