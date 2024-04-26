package org.qingy.irpc.framefork.core.proxy;

/**
 * @Author: QingY
 * @Date: Created in 22:51 2024-04-24
 * @Description:
 */
public interface ProxyFactory {
    public <T> T getProxy(final Class clazz);
}
