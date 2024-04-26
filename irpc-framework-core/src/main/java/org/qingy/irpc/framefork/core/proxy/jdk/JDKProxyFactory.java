package org.qingy.irpc.framefork.core.proxy.jdk;

import org.qingy.irpc.framefork.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @Author: QingY
 * @Date: Created in 22:50 2024-04-24
 * @Description:
 */
public class JDKProxyFactory implements ProxyFactory {

    public JDKProxyFactory() {
    }

    @Override
    public <T> T getProxy(Class clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                new JDKClientInvocationHandler(clazz));
    }
}
