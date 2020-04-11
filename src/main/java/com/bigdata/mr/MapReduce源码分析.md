### MapReduce

##### 源码分析--Client

1. 程序入口

   ```java
   job.waitForCompletion(true);
   ```

2. 提交任务

   ``` java
   submitter.submitJobInternal(Job.this, cluster);
   ```

3. __创建RecordReader__（记录读取器） <关键步骤>

   ```java
   InputFormat<?, ?> input =
     ReflectionUtils.newInstance(job.getInputFormatClass(), conf);
   ...
   (Class<? extends InputFormat<?,?>>) 
         conf.getClass(INPUT_FORMAT_CLASS_ATTR, TextInputFormat.class);
   ```

   根据job拿到用户配置的InputFormatClass，配置项为：mapreduce.job.inputformat.class，默认值为：TextInputFormat.class

4. __创建splits__ <关键步骤>

   ```java
   // 根据用户配置的参数mapreduce.input.fileinputformat.split.minsize
   // 和1比较取出大的值
   long minSize = Math.max(getFormatMinSplitSize(), getMinSplitSize(job));
   // 根据用户配置的参数mapreduce.input.fileinputformat.split.maxsize
   // 如果配置了则取用户配置的，如果没有配置则取Long.MAX_VALUE
   long maxSize = getMaxSplitSize(job);
   
   // generate splits
   List<InputSplit> splits = new ArrayList<InputSplit>();
   // 根据job的输入文件路径，获取文件的元数据
   // 获取文件元数据细节：
   // 会根据用户的配置的mapreduce.input.fileinputformat.list-status.num-threads
   // 启动多个线程去获取数据，否则单线程运行
   List<FileStatus> files = listStatus(job);
   for (FileStatus file: files) {
       Path path = file.getPath();
       long length = file.getLen();
       if (length != 0) {
           BlockLocation[] blkLocations;
           if (file instanceof LocatedFileStatus) {
               blkLocations = ((LocatedFileStatus) file).getBlockLocations();
           } else {
               FileSystem fs = path.getFileSystem(job.getConfiguration());
               blkLocations = fs.getFileBlockLocations(file, 0, length);
           }
           // 永远返回true
           if (isSplitable(job, path)) {
               // 获取文件的block长度
               long blockSize = file.getBlockSize();
               // 先取block长度和maxSize中的小的，再和minSize取大的
               long splitSize = computeSplitSize(blockSize, minSize, maxSize);
   
               long bytesRemaining = length;
               // 如果文件大小溢出split大小10%，则将文件切割掉
               while (((double) bytesRemaining)/splitSize > SPLIT_SLOP) {
                   int blkIndex = getBlockIndex(blkLocations, length-bytesRemaining);
                   // 生成split，offset=length-bytesRemaining，size=splitSize
                   splits.add(makeSplit(path, length-bytesRemaining, splitSize,
                                        blkLocations[blkIndex].getHosts(),
                                        blkLocations[blkIndex].getCachedHosts()));
                   bytesRemaining -= splitSize;
               }
   			// 如果该文件有剩余的，则剩余部分生成splits，offset=length-bytesRemaining，size=bytesRemaining
               if (bytesRemaining != 0) {
                   int blkIndex = getBlockIndex(blkLocations, length-bytesRemaining);
                   splits.add(makeSplit(path, length-bytesRemaining, bytesRemaining,
                                        blkLocations[blkIndex].getHosts(),
                                        blkLocations[blkIndex].getCachedHosts()));
               }
           } else { // not splitable
               splits.add(makeSplit(path, 0, length, blkLocations[0].getHosts(),
                                    blkLocations[0].getCachedHosts()));
           }
       } else { 
           //Create empty hosts array for zero length files
           splits.add(makeSplit(path, 0, length, new String[0]));
       }
   }
   ...
   
   return splits;
   ```

5. 根据生成的split个数，设置map的任务个数   mapreduce.job.maps

   ```java
   conf.setInt(MRJobConfig.NUM_MAPS, maps);
   ```

5. 设置配置文件   ...  

   主要设置submitJobDir

   如果是YarnRunner采用配置项：yarn.app.mapreduce.am.staging-dir或者默认值 /tmp/hadoop-yarn/staging

   如果是LocalJobRunner采用配置项：mapreduce.jobtracker.staging.root.dir或者默认值

   /tmp/hadoop/mapred/staging

6. 提交任务

   ```java
   // submitClient 会根据配置文件mapreduce.framework.name来创建YarnRunner或者LocalJobRunner，如果是LocalJobRunner则map任务的数量为1
   status = submitClient.submitJob(
       jobId, submitJobDir.toString(), job.getCredentials());
   ```

### MapTask

##### input

1. 如果采用的是Yarn提交任务，则是通过YarnChild类中main方法执行MapTask或者ReduceTask

   ```java
   childUGI.doAs(new PrivilegedExceptionAction<Object>() {
     @Override
     public Object run() throws Exception {
       // use job-specified working directory
       FileSystem.get(job).setWorkingDirectory(job.getWorkingDirectory());
       // run MapTask or ReduceTask
       taskFinal.run(job, umbilical); // run the task
       return null;
     }
   });
   ```

2. MapTask类中

   ```java
   // 初始化JobContextImpl（JobContext上下文）、TaskAttemptContextImpl
   initialize(job, getJobID(), reporter, useNewApi);
   ```

   ```java
   // 使用新旧API
   if (useNewApi) {
     runNewMapper(job, splitMetaInfo, umbilical, reporter);
   } else {
     runOldMapper(job, splitMetaInfo, umbilical, reporter);
   }
   ```

   ```java
   try {
     // 调用LineRecordReader类中的initialize方法
     input.initialize(split, mapperContext);
     // 封装了mapperContext--->mapContext(reader,writer...)
     // 通过NewTrackingRecordReader方法，创建了recordReader
     // this.real = inputFormat.createRecordReader(split, taskContext);
     mapper.run(mapperContext);
     mapPhase.complete();
     setPhase(TaskStatus.Phase.SORT);
     statusUpdate(umbilical);
     input.close();
     input = null;
     output.close(mapperContext);
     output = null;
   } finally {
     closeQuietly(input);
     closeQuietly(output, mapperContext);
   }
   ```

3. 上图中的inputFormat.createRecordReader(split, taskContext); 具体逻辑

   ```java
   createRecordReader(InputSplit split,
                      TaskAttemptContext context) {
   String delimiter = context.getConfiguration().get(
       "textinputformat.record.delimiter");
   byte[] recordDelimiterBytes = null;
   if (null != delimiter)
     recordDelimiterBytes = delimiter.getBytes(Charsets.UTF_8);
   return new LineRecordReader(recordDelimiterBytes);
   ```

   根据用户配置的textinputformat.record.delimiter设置行分隔符

4. LineRecordReader中的initialize方法解析

   ```java
   final Path file = split.getPath();
   
   // open the file and seek to the start of the split
   final FileSystem fs = file.getFileSystem(job);
   fileIn = fs.open(file);
   ...
   fileIn.seek(start);
   in = new UncompressedSplitLineReader(
       fileIn, job, this.recordDelimiterBytes, split.getLength());
   filePosition = fileIn;
   ...
       
   // 如果这里不是第一个split，我们将会丢弃第一条记录，这样就解决了hdfs在进行文件切割时会将一行数据切割到
   // 两个文件中
   if (start != 0) {
       start += in.readLine(new Text(), 0, maxBytesToConsume(start));
   }
   this.pos = start;
   ```

   ```java
   // 会根据用户配置的行分隔符调用不同的行记录读取方法
   if (this.recordDelimiterBytes != null) {
     return readCustomLine(str, maxLineLength, maxBytesToConsume);
   } else {
     return readDefaultLine(str, maxLineLength, maxBytesToConsume);
   }
   ```

5. LineRecordReader类中提供了记录读取方法：

   nextKeyValue():	读取一条记录对key、value赋值，并返回boolean值

   getCurrentKey()

   getCurrentValue()

##### output

1. 在MapTask类中

   ```java
   // 根据是否有reduce来初始化output类
   if (job.getNumReduceTasks() == 0) {
     output = 
       new NewDirectOutputCollector(taskContext, job, umbilical, reporter);
   } else {
     output = new NewOutputCollector(taskContext, job, umbilical, reporter);
   }
   ```

   看NewOutputCollector类

   ```java
   // 创建map端输出收集器，默认为MapOutputBuffer.class
   collector = createSortingCollector(job, reporter);
   partitions = jobContext.getNumReduceTasks();
   // 根据reduce任务个数确定分区数，如果是多个分区则会根据用户配置的mapreduce.job.partitioner.class
   // 拿到自定的分区器，否则是默认的分区器：HashPartitioner.class
   // 分区方法：(key.hashCode() & Integer.MAX_VALUE) % numReduceTasks
   if (partitions > 1) {
     partitioner = (org.apache.hadoop.mapreduce.Partitioner<K,V>)
       ReflectionUtils.newInstance(jobContext.getPartitionerClass(), job);
   } else {
     // 如果是一个分区，则创建一个匿名分区器，所有数据会丢到一个分区里
     partitioner = new org.apache.hadoop.mapreduce.Partitioner<K,V>() {
       @Override
       public int getPartition(K key, V value, int numPartitions) {
         return partitions - 1;
       }
     };
   }
   ```

2. __MapOutputBuffer__ 分析

   map输出的K、V会根据分区器算出P，形成K-V-P三元数组，会将K、V序列化

   MapOutputBuffer使用的是环形缓冲区：

   ##### TODO

### ReduceTask

	##### TODO

