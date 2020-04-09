package com.bigdata.zookeeper.common;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkUtils {

    private ZooKeeper zooKeeper = null;
    private String path = "node01:2181";
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private DefaultWatch watcher = new DefaultWatch(countDownLatch);

    public ZooKeeper connect(String root) {
        try {
            zooKeeper = new ZooKeeper(path + "/" + root, 1000, watcher);
            countDownLatch.await();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return zooKeeper;
    }

}
