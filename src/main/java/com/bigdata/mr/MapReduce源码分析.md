### MapReduce

##### 源码分析

1. 程序入口

   ```java
   job.waitForCompletion(true);
   ```

2. 提交任务

   ```java
   submitter.submitJobInternal(Job.this, cluster);
   ```

3. 创建splits 关键步骤

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
4. 根据生成的split个数，设置map的任务个数   mapreduce.job.maps

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