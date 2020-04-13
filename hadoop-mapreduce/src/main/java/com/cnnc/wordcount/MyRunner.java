package com.cnnc.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

public class MyRunner {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration(true);

        GenericOptionsParser parser = new GenericOptionsParser(configuration, args);
        String[] othargs = parser.getRemainingArgs();

        // 适配windows异构平台运行
        configuration.set("mapreduce.app-submission.cross-platform","true");

        // 设置mr本地运行 本地模式运行需要配置hadoop环境，且需要拷贝hadoop.dll到c:\windows\system32目录下
        configuration.set("mapreduce.framework.name", "local");

        // 创建JOB
        Job job = Job.getInstance(configuration);
        job.setJobName("Word Count");
        job.setJar("E:\\hadoop\\hadoop-more\\target\\hadoop-more-1.0-SNAPSHOT.jar");
        job.setJarByClass(MyRunner.class);

        // 设置输入路径
        Path inPath = new Path(othargs[0]);
        TextInputFormat.addInputPath(job, inPath);

        // 设置输出路径
        Path outPath = new Path(othargs[1]);
        if (outPath.getFileSystem(configuration).exists(outPath)) {
            outPath.getFileSystem(configuration).delete(outPath, true);
        }
        TextOutputFormat.setOutputPath(job, outPath);

        // 设置Mapper
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 设置Reduce
        job.setReducerClass(MyReducer.class);

        // 运行
        job.waitForCompletion(true);
    }

}
