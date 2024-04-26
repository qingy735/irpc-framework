package org.qingy.irpc.framefork.core.server;

import org.qingy.irpc.framefork.interfaces.DataService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: QingY
 * @Date: Created in 22:06 2024-04-24
 * @Description:
 */
public class DataServiceImpl implements DataService {
    @Override
    public String sendData(String body) {
        System.out.println("己收到的参数长度：" + body.length());
        return "success";
    }

    @Override
    public List<String> getList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("qingy1");
        arrayList.add("qingy2");
        arrayList.add("qingy3");
        return arrayList;
    }
}
