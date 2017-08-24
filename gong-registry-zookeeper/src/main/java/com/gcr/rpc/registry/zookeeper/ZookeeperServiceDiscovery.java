package com.gcr.rpc.registry.zookeeper;

import com.gcr.common.util.CollectionUtil;
import com.gcr.rpc.registry.ServiceDiscovery;
import com.gcr.rpc.registry.ServiceRegistry;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 基于zookeeper的服务发现接口实现
 * Created by gongchunru
 * Date：2017/8/24.
 * Time：13:02
 */
public class ZookeeperServiceDiscovery implements ServiceDiscovery{

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceDiscovery.class);

    private String zkAddress;

    public ZookeeperServiceDiscovery(String zkAddress) {
        this.zkAddress = zkAddress;
    }


    @Override
    public String discover(String serviceName) {
        //创建zookeeper客户端
        ZkClient zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        logger.debug("connect zookeeper");

        try {
            //获取service节点
            String servicePath = Constant.ZK_REGISTRY_PATH + "/" + serviceName;
            if (!zkClient.exists(servicePath)){
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            List<String> addressList = zkClient.getChildren(servicePath);
            if (CollectionUtil.isEmpty(addressList)){

            }


        }finally {
            zkClient.close();
        }


        return null;
    }
}
