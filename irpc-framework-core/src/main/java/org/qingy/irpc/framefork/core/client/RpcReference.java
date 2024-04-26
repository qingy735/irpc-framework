package org.qingy.irpc.framefork.core.client;

import org.qingy.irpc.framefork.core.proxy.ProxyFactory;
import org.qingy.irpc.framefork.core.proxy.jdk.JDKProxyFactory;

/**
 * @Author: QingY
 * @Date: Created in 22:21 2024-04-24
 * @Description:
 */
public class RpcReference {
    private ProxyFactory proxyFactory;

    public RpcReference(JDKProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * 根据接口类型获取代理对象
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> tClass) throws Throwable {
        return proxyFactory.getProxy(tClass);
    }

}
