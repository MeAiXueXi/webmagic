package com.lidong.crawler.utils;

import cn.wawi.common.spider.entity.ItemInfo;
import cn.wawi.common.spider.entity.ItemTransferInfo;
import cn.wawi.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Site;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 *   插入事项
 *   李东
 *   2019.11.27
 */
public class InsertItems  {


    InsertItems(){}
    public InsertItems(List<ItemTransferInfo> var){
        this.baseInfos = var;
    }

    String key = "511411000000";
    String area = "mss";
    List<ItemTransferInfo> baseInfos;
    volatile ConcurrentHashMap<String, ItemInfo> map = new ConcurrentHashMap<>(16);

    final static Site site = Site.me().setDomain("http://mss.sczwfw.gov.cn/app/powerDutyList/getThImplement").setCycleRetryTimes(2000).setTimeOut(2000);

    @SuppressWarnings("unchecked")
    public List<String> processAllItem() throws IOException, ClassNotFoundException {
        List<String> result = new ArrayList<>();
        String file = "D:\\ps_zw_tfxq\\itemUrlList.txt";
        String mapFile = "D:\\ps_zw_tfxq\\mapItem.txt";
        if (FileUtils.getFile(file).exists() && FileUtils.getFile(mapFile).exists()) {
            FileInputStream fos = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fos);
            result = (List<String>) objectInputStream.readObject();
            objectInputStream.close();

            FileInputStream mapFos = new FileInputStream(mapFile);
            ObjectInputStream mapStream = new ObjectInputStream(mapFos);
            map = (ConcurrentHashMap<String, ItemInfo>) mapStream.readObject();
            mapStream.close();
            return result;
        }
        final List<String> resultCopy = result;
        for (ItemTransferInfo baseInfo : baseInfos) {
            System.out.println("获取所有抓取事项列表：" + (baseInfos.indexOf(baseInfo) * 100 / baseInfos.size()) + "%");
            String title = baseInfo.getTitle();

            process(resultCopy, baseInfo, title, baseInfo.getWhetherCity() ? key : "511411000000");
        }

        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
        objectOutputStream.writeObject(resultCopy);
        objectOutputStream.close();

        FileOutputStream mapFos = new FileOutputStream(mapFile);
        ObjectOutputStream mapStream = new ObjectOutputStream(mapFos);
        mapStream.writeObject(map);
        mapStream.close();
        return resultCopy;
    }

    public void process(List<String> resultCopy, ItemTransferInfo baseInfo, String title, String keyTemp) throws IOException {
        Map<String, String> params = new HashMap<>(16);
        params.put("eventName", title);
        params.put("key", keyTemp);
        params.put("eventType", "1");
        params.put("pageSize", "10");
        params.put("page", "1");
        String rawText = HttpClientUtil.doPost(site.getDomain(), params, headers());
        if (title.contains("不含仓储经营")) {
            String a = "";
        }

        JSONObject jsonObject = JSONObject.parseObject(rawText);
        List<ItemInfo> rows = JSONArray.parseArray(jsonObject.getString("rows"), ItemInfo.class);

        Optional<ItemInfo> first = rows.stream().filter(x ->
                StringUtils.isNotEmpty(x.getDirName()) ? (x.getDirName().contains(title) || x.getEventName().contains(title)) : x.getEventName().contains(title))
                .findFirst();
        if (first.isPresent()) {
            ItemInfo itemInfo = first.get();
            if (StringUtils.isNotEmpty(itemInfo.getDirName())) {
                //找到目录了
                String getChildUrl = String.format("http://%s.sczwfw.gov.cn/app/thing/findByThDirectory?eventName=%s&key=%s&eventType=1&deptCode=&percenceTimesOnline=&thDirectoryId=%s",
                        area, URLEncoder.encode(title), keyTemp, itemInfo.getDirId());
                rawText = HttpClientUtil.doGet(getChildUrl);

                List<ItemInfo> childRows = JSONArray.parseArray(rawText, ItemInfo.class);
                for (int i = 0; i < childRows.size(); i++) {
                    ItemInfo childRow = childRows.get(i);
//                        if (baseInfo.getChildTitles().contains(childRow.getEventName())) {
                    String detailUrl = String.format("http://%s.sczwfw.gov.cn/app/workGuide/detail?id=%s&shardKey=%s&typeflag=3",
                            area, childRow.getIdForStr(), childRow.getShardKey());
                    resultCopy.add(detailUrl);
                    map.put(detailUrl, itemInfo);
//                        }
                }
            } else {
                //没有目录的事项
                String detailUrl = String.format("http://%s.sczwfw.gov.cn/app/workGuide/detail?id=%s&shardKey=%s&typeflag=3",
                        area, itemInfo.getIdForStr(), itemInfo.getShardKey());
                resultCopy.add(detailUrl);
                map.put(detailUrl, itemInfo);
            }
        } else {
            List<String> childTitles = baseInfo.getChildTitles();
            System.out.println("未找到目录事项：" + title + ",开始查找子事项..");
            //子事项去找
            for (int i = 0; i < childTitles.size(); i++) {
                String childTitle = childTitles.get(i);
                params.put("eventName", childTitle);
                params.put("key", keyTemp);
                params.put("eventType", "1");
                params.put("pageSize", "10");
                params.put("page", "1");
                rawText = HttpClientUtil.doPost(site.getDomain(), params, headers());

                jsonObject = JSONObject.parseObject(rawText);
                rows = JSONArray.parseArray(jsonObject.getString("rows"), ItemInfo.class);
                first = rows.stream()
                        .filter(x -> x.getEventName().contains(childTitle))
                        .findFirst();
                if (first.isPresent()) {
                    ItemInfo itemInfo = first.get();
                    String detailUrl = String.format("http://%s.sczwfw.gov.cn/app/workGuide/detail?id=%s&shardKey=%s&typeflag=3",
                            area, itemInfo.getIdForStr(), itemInfo.getShardKey());
                    resultCopy.add(detailUrl);
                    map.put(detailUrl, itemInfo);
                } else {
                    //还是没有
//                    System.out.println("没有找到子事项：" + childTitle + "--------去县上查询..");
//                    if (!"511421000000".equals(keyTemp)) {
//                        process(resultCopy, baseInfo, title, "511421000000");
//                    } else {
//                        System.out.println("县上也没有找到子事项：" + childTitle);
//                    }
                }
            }
//
//
                if (!"0".equals(rows.get(0).getDirId())) {
                    System.out.println("找第一个里面是不是存在:" + title);
                    String getChildUrl = String.format("http://%s.sczwfw.gov.cn/app/thing/findByThDirectory?eventName=%s&key=%s&eventType=1&deptCode=&percenceTimesOnline=&thDirectoryId=%s",
                            area, URLEncoder.encode(title), keyTemp, rows.get(0).getDirId());
                    rawText = HttpClientUtil.doGet(getChildUrl);
                    List<ItemInfo> childRows = JSONArray.parseArray(rawText, ItemInfo.class);
                    Optional<ItemInfo> first1 = childRows.stream().filter(x ->
                            StringUtils.isNotEmpty(x.getDirName()) ? (x.getDirName().contains(title) || x.getEventName().contains(title)) : x.getEventName().contains(title))
                            .findFirst();
                    if (first1.isPresent()) {
                        //找到了
                        ItemInfo itemInfo = first1.get();
                        String detailUrl = String.format("http://%s.sczwfw.gov.cn/app/workGuide/detail?id=%s&shardKey=%s&typeflag=3",
                                area, itemInfo.getIdForStr(), itemInfo.getShardKey());
                        resultCopy.add(detailUrl);

                        //把目录加上
                    } else {
                        System.out.println("还是未找到事项：" + title);
                    }
                }
        }
    }


    static Map<String, String> headers() {
        Map<String, String> var = new HashMap<>(16);
        var.put("Server", "nginx");
        var.put("Date", new Date().toString());
        var.put("Connection", "keep-alive");
        var.put("X-Content-Type-Options", "nosniff");
        var.put("X-XSS-Protection", "1; mode=block");
        var.put("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        var.put("Pragma", "no-cache");
        var.put("Expires", "0");
        var.put("X-Application-Context", "egov-portal-ui:8080");
        var.put("Content-Encoding", "gzip");
        return var;
    }
}
