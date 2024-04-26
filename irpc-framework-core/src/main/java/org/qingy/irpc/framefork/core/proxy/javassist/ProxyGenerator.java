package org.qingy.irpc.framefork.core.proxy.javassist;

import java.lang.reflect.InvocationHandler;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: QingY
 * @Date: Created in 23:04 2024-04-24
 * @Description:
 */
// public class ProxyGenerator {
//     private static final AtomicInteger counter = new AtomicInteger(1);
//
//     private static ConcurrentHashMap<Class<?>, Object> proxyInstanceCache = new ConcurrentHashMap<>();
//
//     public static Object newProxyInstance(ClassLoader classLoader, Class<?> targetClass, InvocationHandler invocationHandler) {
//         if (proxyInstanceCache.containsKey(targetClass)) {
//             return proxyInstanceCache.get(targetClass);
//         }
//
//     }
//
// }
