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

   1. 线性字节数组

   2. 两端方向放KV、索引（固定宽度：4个int）

   3. 数据存储结构：

      1. P	分区信息
      2. K    KEY信息
      3. V    VALUE信息

   4. 如果数据填充到阈值：80%，启动线程

      1. 快速排序80%数据，同时map输出的线程向剩余的空间写
      2. 快速排序的过程：是比较key排序，但是移动的是索引

   5. 溢写

      根据索引的排序溢写数据，排序是二次排序，第一次是P排序保证相同key进到相同的分区中，第二次是key排序，相同P中的key是有序的为了保证后续reduce任务一次IO即可解决所有数据

   ###### MapOutputBuffer初始化过程

   ​	spillper = 0.8

   ```java
   final float spillper =
     job.getFloat(JobContext.MAP_SORT_SPILL_PERCENT, (float)0.8);
   ```

   ​	sortmb = 100M

   ```java
   final int sortmb = job.getInt(JobContext.IO_SORT_MB, 100);
   ```

   ​	sorter = QuickSort

   ```java
   sorter = ReflectionUtils.newInstance(job.getClass("map.sort.class",
         QuickSort.class, IndexedSorter.class), job);
   ```

   ​	comparator = job.getOutputKeyComparator();

   ```java
   // 优先取用户覆盖的自定义排序比较器
   // 保底取key这个类型自身的比较器
   comparator = job.getOutputKeyComparator();
   ```

   ​	SpillThread

   ​		sortAndSpill()

   ```java
   if (combinerRunner == null) {
     // spill directly
     DataInputBuffer key = new DataInputBuffer();
     while (spindex < mend &&
         kvmeta.get(offsetFor(spindex % maxRec) + PARTITION) == i) {
       final int kvoff = offsetFor(spindex % maxRec);
       int keystart = kvmeta.get(kvoff + KEYSTART);
       int valstart = kvmeta.get(kvoff + VALSTART);
       key.reset(kvbuffer, keystart, valstart - keystart);
       getVBytesForOffset(kvoff, value);
       writer.append(key, value);
       ++spindex;
     }
   } else {
     int spstart = spindex;
     while (spindex < mend &&
         kvmeta.get(offsetFor(spindex % maxRec)
                   + PARTITION) == i) {
       ++spindex;
     }
     // Note: we would like to avoid the combiner if we've fewer
     // than some threshold of records for a partition
     if (spstart != spindex) {
       combineCollector.setWriter(writer);
       RawKeyValueIterator kvIter =
         new MRResultIterator(spstart, spindex);
       combinerRunner.combine(kvIter, combineCollector);
     }
   }
   ```

### ReduceTask

1. 如果是yarn资源管理器，则是通过YarnChild类启动ReduceTask任务，前置步骤跟上面MapTask一样

   ```java
   // 通过配置项mapred.combiner.class取combinerClass否则为空，同时检测该类是否为Reduce.class的子类
   Class combinerClass = conf.getCombinerClass();
   // 通过上线combinerClass生成combineCollector
   CombineOutputCollector combineCollector = 
     (null != combinerClass) ? 
    new CombineOutputCollector(reduceCombineOutputCounter, reporter, conf) : null;
   ```

2. 执行shuffle过程

   ```java
   ShuffleConsumerPlugin.Context shuffleContext = 
     new ShuffleConsumerPlugin.Context(getTaskID(), job, FileSystem.getLocal(job), umbilical, 
                 super.lDirAlloc, reporter, codec, 
                 combinerClass, combineCollector, 
                 spilledRecordsCounter, reduceCombineInputCounter,
                 shuffledMapsCounter,
                 reduceShuffleBytes, failedShuffleCounter,
                 mergedMapOutputsCounter,
                 taskStatus, copyPhase, sortPhase, this,
                 mapOutputFile, localMapFiles);
   shuffleConsumerPlugin.init(shuffleContext);
   
   rIter = shuffleConsumerPlugin.run();
   ```

3. 如果不是local模式，通过Fetcher类实现shuffle的过程

   ```java
   // If merge is on, block
   merger.waitForResource();
   
   // Get a host to shuffle from
   host = scheduler.getHost();
   metrics.threadBusy();
   
   // Shuffle
   copyFromHost(host);
   ```

4. 创建taskContext任务上下文、创建reducer、创建RecordWriter

3. ```java
   try {
     // 一组数据调用一次reduce
     while (context.nextKey()) {
       reduce(context.getCurrentKey(), context.getValues(), context);
       // If a back up store is used, reset it
       Iterator<VALUEIN> iter = context.getValues().iterator();
       if(iter instanceof ReduceContext.ValueIterator) {
         ((ReduceContext.ValueIterator<VALUEIN>)iter).resetBackupStore();        
       }
     }
   }
   ```

4. context.nextKey() 代码分析

   ```java
   // 首先数据通过shuffle根据map端生成K-V-P三元组其中的P，相同的P会拉到同一个reduce，而一个reduce中
   // 可能会有多个key
   // 判断hasMore为true且nextKeyIsSame表示还是同一组key的遍历，则继续取下一条数据
   // 否则返回false，上一层迭代器会去取下一组的key，后面实现一样
   while (hasMore && nextKeyIsSame) {
     nextKeyValue();
   }
   if (hasMore) {
     if (inputKeyCounter != null) {
       inputKeyCounter.increment(1);
     }
     return nextKeyValue();
   } else {
     return false;
   }
   ```

##### 总结

1. MR框架中充分利用了迭代器模式：避免将数据拉到内存中造成OOM

2. 计算向数据移动：在client提交任务时，组装好conf文件，文件中包含split信息（file、offset、length、hosts）、资源文件路径（jar包在hdfs中的位置信息），发起计算时datanode会通过配置信息拉取jar包，计算本地file

3. InputFormat、RecordReader、RecordWriter

4. MapOutputBuffer map端溢写数据

5. 比较器选择

   | mapTask                   | reduceTask                |
   | ------------------------- | ------------------------- |
   |                           | 1、用户自定义的分组比较器 |
   | 1、用户自定义的排序比较器 | 2、用户定义的排序比较器   |
   | 2、KEY自身的排序比较器    | 3、KEY自身的排序比较器    |

   | 组合方式：             | map                    | reduce                   |
   | :--------------------- | :--------------------- | ------------------------ |
   | 不设置排序和分组比较器 | 取key自身的排序比较器  | 取key自身的排序比较器    |
   | 设置了排序             | 用户定义的排序比较器   | 用户定义的排序比较器     |
   | 设置了分组             | 取key自身的排序比较器  | 取用户自定义的分组比较器 |
   | 设置了排序和分组       | 取用户定义的排序比较器 | 取用户自定义的分组比较器 |