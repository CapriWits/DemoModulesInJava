package com.hypocrite30.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.common.FileChangeWatcher;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * @Description: zookeeper 在 java 的使用
 * @Author: Hypocrite30
 * @Date: 2021/11/13 20:39
 */
public class TestZookeeper {
    public static void main(String[] args) throws Exception {
        create();
        // delete();
    }

    /**
     * 删除节点.
     * 删除节点前，需要先查询节点的状态（cversion）。通过getData来查询这个版本。
     * 设计是为了保证删除的节点是你想删除的那个。
     */
    public static void delete() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.248.128:2181", 10000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
            }
        });
        Stat stat = new Stat();
        System.out.println(stat);

        zooKeeper.getData("/parent/sequence0000000001", false, stat);

        System.out.println(stat.getCversion());

        zooKeeper.delete("/parent/sequence0000000001", stat.getCversion());
    }

    /**
     * 查询节点中存储的数据。 相当于根据key获取value
     */
    public static void get() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.89.140:2181", 10000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
            }
        });
        // 获取数据
        byte[] datas = zooKeeper.getData("/parent", false, null);
        System.out.println("节点/parent中存储的数据是：" + new String(datas));
    }

    /**
     * 查询节点， 相当于遍历Key
     */
    public static void list() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.89.140:2181", 10000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
            }
        });
        // 遍历zk中所有的节点。
        listAll(zooKeeper, "/");

        /*List<String> children = zooKeeper.getChildren("/a", false);
        System.out.println(children.size());
        for(String child : children){
            System.out.println(child);
        }*/
    }

    private static void listAll(ZooKeeper zooKeeper, String path) throws Exception {
        // 获取当前节点的所有子节点。
        List<String> children = zooKeeper.getChildren(path, false);
        for (String child : children) {
            String currentNodeName = "/".equals(path) ? (path + child) : (path + "/" + child);
            System.out.println(currentNodeName);
            listAll(zooKeeper, currentNodeName);
        }
    }

    /**
     * 什么是会话？
     * 持久、长期、有状态的对象。
     * 使用Java远程访问zk，步骤是：
     * 1、 创建客户端
     * 2、 使用客户端发送命令
     * 3、 处理返回结果
     * 4、 回收资源
     */
    public static void create() throws IOException, KeeperException, InterruptedException {
        // 创建客户端对象
        ZooKeeper zooKeeper = new ZooKeeper("192.168.248.128:2181", 10000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("watch中的方法执行");
            }
        });

        // 创建一个节点
        String result = zooKeeper.create("/parent", "parent data".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("创建parent节点结果：" + result);

        // 创建一个临时节点
        String tmpResult =
                zooKeeper.create("/parent/tmp", null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        System.out.println("创建/parent/tmp节点结果：" + tmpResult);
        // 创建一个带序号的节点
        String seqResult =
                zooKeeper.create("/parent/sequence", null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println("创建/parent/sequence节点结果：" + seqResult);

        // 关闭客户端
        zooKeeper.close();
    }
}
