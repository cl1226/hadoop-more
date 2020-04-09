package com.bigdata.zookeeper.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallback implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

    private ZooKeeper zooKeeper;
    private MyConf myConf;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public WatchCallback(ZooKeeper zooKeeper, MyConf myConf) {
        this.zooKeeper = zooKeeper;
        this.myConf = myConf;
    }

    public void await() {
        try {
            zooKeeper.exists("/AppConf", this, this, "abc");
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        if (stat != null) {
            String s = new String(data);
            myConf.setConf(s);
            countDownLatch.countDown();
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (stat != null) {
            zooKeeper.getData("/AppConf", this, this, ctx);
        }
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                zooKeeper.getData("/AppConf", this, this, "abc");
                break;
            case NodeDeleted:
                myConf.setConf("");
                countDownLatch = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zooKeeper.getData("/AppConf", this, this, "abc");
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }
    }
}
