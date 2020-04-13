package com.cnnc.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WatchCallback implements Watcher, AsyncCallback.Create2Callback,
        AsyncCallback.Children2Callback, AsyncCallback.StatCallback {

    private ZooKeeper zooKeeper;
    private String threadName;
    private String pathName;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public WatchCallback(ZooKeeper zooKeeper, String threadName) {
        this.zooKeeper = zooKeeper;
        this.threadName = threadName;
    }

    public void tryLock() {
        System.out.println(threadName + " creating...");
        zooKeeper.create("/lock", threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, this, "aaa");

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unLock() {
        try {
            zooKeeper.delete(pathName, -1);
            System.out.println(threadName + " work over.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, String name, Stat stat) {
        if (name != null) {
            System.out.println(threadName + " create node: " + name);
            this.pathName = name;
            zooKeeper.getChildren("/", false, this, "bbb");
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        Collections.sort(children);
        int i = children.indexOf(pathName.substring(1));

        if (i == 0) {
            System.out.println(threadName + " is first...");
//            try {
//                zooKeeper.setData("/", threadName.getBytes(), -1);
//            } catch (KeeperException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            countDownLatch.countDown();
        } else {
            zooKeeper.exists("/" + children.get(i-1), this, this, "ccc");
        }

    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {

    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zooKeeper.getChildren("/", this, this, "ddd");
                break;
            case NodeDataChanged:
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
