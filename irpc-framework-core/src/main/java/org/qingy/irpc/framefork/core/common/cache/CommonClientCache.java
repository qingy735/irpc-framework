package org.qingy.irpc.framefork.core.common.cache;

import org.qingy.irpc.framefork.core.common.RpcInvocation;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公用缓存 存储请求队列等公共信息
 *
 * @Author: QingY
 * @Date: Created in 21:51 2024-04-24
 * @Description:
 */
public class CommonClientCache {
    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue(100);
    public static Map<String, Object> RESP_MAP = new ConcurrentHashMap<>();
}
