package com.cnnc.lock;

import com.cnnc.common.ZkUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZkLockDemo {

    private ZooKeeper zooKeeper = null;

    @Before
    public void init() {
        ZkUtils zkUtils = new ZkUtils();
        zooKeeper = zkUtils.connect("testLock");
    }

    @Test
    public void testLock() {
        for (int i = 0; i < 10; i++) {
            new Thread(){
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    WatchCallback watchCallback = new WatchCallback(zooKeeper, threadName);

                    watchCallback.tryLock();

                    System.out.println(threadName + " working...");

//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    watchCallback.unLock();
                }
            }.start();
        }

        while (true) {

        }
    }

    @After
    public void close() {
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
