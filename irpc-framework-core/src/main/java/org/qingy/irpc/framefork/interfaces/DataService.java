package org.qingy.irpc.framefork.interfaces;

import java.util.List;

/**
 * @Author: QingY
 * @Date: Created in 22:03 2024-04-24
 * @Description:
 */
public interface DataService {
    public String sendData(String body);

    public List<String> getList();
}
