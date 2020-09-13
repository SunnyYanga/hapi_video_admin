package com.yang.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yg
 * @date 2020/8/18 14:35
 */
public class ZKCurator {

    private CuratorFramework client = null;

    final static Logger log = LoggerFactory.getLogger(ZKCurator.class);

    public ZKCurator(CuratorFramework client) {
        this.client = client;
    }

    public void init() {
        client = client.usingNamespace("admin");
        try {
            if (client.checkExists().forPath("/bgm") == null) {
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT) // 持久节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE) //匿名权限
                        .forPath("/bgm");
                log.info("zookeeper初始化成功");
                log.info("zookeeper状态{}" + client.isStarted());
            }
        } catch (Exception e) {
            log.error("zookeeper初始化错误");
            e.printStackTrace();
        }
    }

    /**
     * 增加或者删除bgm, 向zk-server创建子节点，供小程序后端监听
     * @param bgmId
     * @param operObj
     */
    public void sendBgmOperator(String bgmId, String operObj) {
        try {

            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)		// 节点类型：持久节点
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)			// acl：匿名权限
                    .forPath("/bgm/" + bgmId, operObj.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
