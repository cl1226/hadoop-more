### hive
##### hive执行任务的mapper个数确定
1. 文件分割算法
  hive.input.format = org.apache.hadoop.hive.ql.io.HiveInputFormat
  mapper个数由以下几个参数确定
  + mapred.min.split.size   默认为1
  + mapred.max.split.size   hive2.1.1默认为256000000
  + dfs.block.size          hadoop2.0以后默认为134217728(128MB)
  
 ```java
 splitSize =  Math.max(minSize, Math.min(maxSize, blockSize))
 ```
 
 示例：
 
    mapred.min.split.size=1
    mapred.max.split.size=256000000
    dfs.block.size=134217728
    文件大小为943MB
    splitSize = Math.max(1, Math.min(256000000, 134217728)) = 134217728 = 128MB
    mapper个数 = 8
  
-------------- 
2. host选择算法
  hive.input.format = org.apache.hadoop.hive.ql.io.CombineHiveInputFormat
  ```java
    long left = locations[i].getLength();
    long myOffset = locations[i].getOffset();
    long myLength = 0;
    do {
 	    if (maxSize == 0) {
 		    myLength = left;
 	    } else {
 	      if (left > maxSize && left < 2 * maxSize) {
 	        myLength = left / 2;
 	      } else {
 	        myLength = Math.min(maxSize, left);
 	      }
 	    }
 	    OneBlockInfo oneblock = new OneBlockInfo(path, myOffset,
 	      myLength, locations[i].getHosts(), locations[i]
 	      .getTopologyPaths());
 	    left -= myLength;
 	    myOffset += myLength;
 	    blocksList.add(oneblock);
    } while (left > 0);
  ```
  
  示例：
  
    mapred.min.split.size=1
    mapred.max.split.size=256000000 (244.14MB)
    dfs.block.size=134217728  (128MB)
    文件大小为943MB
    
    块划分为：244MB  244MB  244MB 211MB
    mapper个数=4
