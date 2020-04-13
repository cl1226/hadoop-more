package com.cnnc.config;

import com.cnnc.common.ZkUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZkConfigDemo {

    private ZooKeeper zooKeeper = null;

    @Before
    public void init() {
        ZkUtils zkUtils = new ZkUtils();
        zooKeeper = zkUtils.connect("configurations");
    }

    @Test
    public void getConf() {
        MyConf myConf = new MyConf();
        WatchCallback watchCallback = new WatchCallback(zooKeeper, myConf);

        watchCallback.await();
        while (true) {
            if (myConf.getConf().equals("")) {
                System.out.println("配置丢失");
                watchCallback.await();
            } else {
                System.out.println(myConf.getConf());
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
