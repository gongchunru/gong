package com.gcr.rpc.registry.zookeeper;

import com.gcr.rpc.registry.ServiceRegistry;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于zookeeper 的服务注册接口实现
 * Created by gongchunru
 * Date：2017/8/24.
 * Time：13:05
 */
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);


    private final ZkClient zkClient;

    public ZookeeperServiceRegistry(String zkAddress) {
        //创建 Zookeeper 客户端
        zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT,Constant.ZK_CONNECTION_TIMEOUT);
        logger.debug("connect zookeeper");

    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        //创建registry节点（持久）
        String registryPath = Constant.ZK_REGISTRY_PATH;
        if (!zkClient.exists(registryPath)){
            zkClient.createPersistent(registryPath);
            logger.debug("create registry node:{}",registryPath);
        }

        //创建service 节点（持久）
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)){
            zkClient.createPersistent(servicePath);
            logger.debug("create service node: {}",serviceAddress);
        }

        //创建address 节点 （临时）
        String addressPath = servicePath + "/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath,serviceAddress);
        logger.debug("create address node:{}",addressNode);
    }
}
