package com.lidong.crawler.main;


import cn.wawi.common.spider.entity.ItemBaseInfo;
import cn.wawi.common.spider.entity.ItemTransferInfo;
import cn.wawi.common.spider2.utils.Config;
import cn.wawi.common.spider2.utils.InsertItems;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.istack.NotNull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查找 事项的网页链接
 * @author 李东
 * @version 1.0
 * @date 2020/12/6 23:01
 */
public class findItemWebLinks {

    public static void main(String[] args) {
        Config.initPlugin();
        try {
            InsertItems processor = new InsertItems(processUrlList());
            processor.processAllItem();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("出现异常停止");
        }
    }


    @NotNull
    private static List<ItemTransferInfo> processUrlList() throws IOException {
        String fileToString = FileUtils.readFileToString(FileUtils.getFile("/Users/lidong/Desktop/config/Seperate/item.txt"), "utf-8");
        JSONObject jsonObject = JSONObject.parseObject(fileToString);
        List<ItemBaseInfo> baseInfos = JSONArray.parseArray(jsonObject.getString("Sheet1"), ItemBaseInfo.class);
        List<ItemTransferInfo> list = new ArrayList<>();
        for (ItemBaseInfo itemBaseInfo : baseInfos) {
            if (StringUtils.isNotEmpty(itemBaseInfo.getTitle())) {
                ItemTransferInfo itemTransferInfo = new ItemTransferInfo();
                itemTransferInfo.setTitle(itemBaseInfo.getTitle());
                itemTransferInfo.getChildTitles().add(itemBaseInfo.getChildTitle());
                itemTransferInfo.setWhetherCity("√".equals(itemBaseInfo.getShi()));
                itemTransferInfo.setWhetherCounty("√".equals(itemBaseInfo.getXian()));
                list.add(itemTransferInfo);
            } else {
                ItemTransferInfo transferInfo = list.get(list.size() - 1);
                String childTitle = itemBaseInfo.getChildTitle();
                transferInfo.getChildTitles().add(childTitle);
            }
        }
        return list;
    }

}
