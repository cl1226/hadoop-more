### MapReduce

##### 开发步骤

1. 配置文件加载	

   ```java
   Configuration configuration = new Configuration(true);
   ```

2. 解析参数  

   ```java
   //解析出传递的以-D开头的参数
   GenericOptionsParser parser = new GenericOptionsParser(configuration, args);
   //获取剩余的参数
   String[] othargs = parser.getRemainingArgs();
   ```

3. 如果是本地运行，需要增加以下配置

   ```java
   //支持windows异构平台运行，需要安装hadoop环境，替换bin目录且拷贝hadoop.dll至
   //c:\windows\system32目录下
   configuration.set("mapreduce.app-submission.cross-platform","true");
   configuration.set("mapreduce.framework.name", "local");
   ```

4. 创建Job

   ```java
   Job job = Job.getInstance(configuration);
   job.setJobName("Word Count");
   //本地运行需要设置jar包路径
   job.setJar("E:\\hadoop\\hadoop-more\\target\\hadoop-more-1.0-SNAPSHOT.jar");
   //运行时程序需要知道入口类
   job.setJarByClass(MyRunner.class);
   ```

5. 设置输入路径

   ```java
   //使用addInputPath 可以添加多个输入路径
   Path inPath = new Path(othargs[0]);
   TextInputFormat.addInputPath(job, inPath);
   ```

6. 设置输出路径

   ```java
   //使用setOutputPath 只能设置一个输出路径
   Path outPath = new Path(othargs[1]);
   if (outPath.getFileSystem(configuration).exists(outPath)) {
       outPath.getFileSystem(configuration).delete(outPath, true);
   }
   TextOutputFormat.setOutputPath(job, outPath);
   ```

7. 设置map

   ```java
   //设置Map、map输出key|value类型
   job.setMapperClass(TMapper.class);
   job.setMapOutputKeyClass(TKey.class);
   job.setMapOutputValueClass(IntWritable.class);
   ```

8. 设置分区器、分区排序器

   ```java
   //设置分区器，保证相同的key落在相同的分区号里
   job.setPartitionerClass(TPartitioner.class);
   //设置排序器，在同一个分区中按照某种顺序排列
   job.setSortComparatorClass(TSortComparator.class);
   ```

9. 设置Combiner

   ```java
   //设置combiner，相当于在map端先做一次reduce，减少shuffle的IO量，比如在做统计的时候
   job.setCombinerClass(TCombiner.class);
   ```

10. 设置分组比较器

    ```java
    //设置分组比较器，用于需要按照某个维度进行聚合
    job.setGroupingComparatorClass(TGroupingComparator.class);
    ```

11. 设置reduce

    ```java
    //最后输出的内容
    job.setReducerClass(MyReducer.class);
    ```

12. 运行

    ```java
    job.waitForCompletion(true);
    ```

