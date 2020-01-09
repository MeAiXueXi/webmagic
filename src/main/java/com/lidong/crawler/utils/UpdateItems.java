package com.lidong.crawler.utils;

import cn.wawi.common.spider.entity.ItemInfo;
import cn.wawi.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Site;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 更新事项
 * 李东
 * 2019.11.27
 */
public class UpdateItems  {
    String key = "511411000000";
    String area = "mss";
    String DIRECOTORY = "http://www.sczwfw.gov.cn/app/thing/findByThDirectory?areaCode=%s&thDirectoryId=%s";

    final static Site site = Site.me().setDomain("http://mss.sczwfw.gov.cn/app/powerDutyList/getThImplement").setCycleRetryTimes(2000).setTimeOut(2000);
    /**
     * 更新爬虫链接
     */
    public void updateLink(String... areaCode) {
        List<String> list;
        for (String k : areaCode) {
            if (!key.equals(k)) {
                //抓取其他地区
                list = Db.query("select b.itemName  from ap_publish_item a ,ap_crawler_cache b where " +
                        "a.title = b.itemName and a.childQty =0  and a.deptId in (119,134,135,137,138,139,140,141,143)   and b.areaCode is   null; ");
                key = k;

            } else {
                //抓取天府新区
                list = Db.query("select a.title  from  ap_publish_item  a left join ap_crawler_cache b  on a.title =b.itemName    where   a.childQty =0 and b.itemName is null group by a.title;");
            }
            List<String> resultCopy = new ArrayList<>();
            for (String title : list
            ) {
                System.out.println("获取所有抓取事项列表：" + (list.indexOf(title) * 100 / list.size()) + "%");
                process(resultCopy, title, k);
            }
        }
    }


    public void process(List<String> resultCopy, String title, String areaCodeTemp) {
        ConcurrentHashMap<String, ItemInfo> map = new ConcurrentHashMap<>(16);
        CrawlerCache crawlerCache = new CrawlerCache();

        Map<String, String> params = new HashMap<>(16);
        params.put("eventName", title);
        params.put("areaCode", areaCodeTemp);
        params.put("eventType", "1");
        System.out.println(params);
        String rawText = post(params);
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(rawText);
        } catch (Exception e) {
            return;
        }
        List<ItemInfo> rows = JSONArray.parseArray(jsonObject.getString("rows"), ItemInfo.class);

        Optional<ItemInfo> first = rows.stream().filter(x ->
                StringUtils.isNotEmpty(x.getDirName()) ? (x.getDirName().contains(title) || x.getEventName().contains(title)) : x.getEventName().contains(title))
                .findFirst();

        String detailUrl;
        boolean notGetSuss = true;//未找到

        if (first.isPresent()) {
            ItemInfo itemInfo = first.get();
            if (StringUtils.isNotEmpty(itemInfo.getDirName())) {
                String getChildUrl = String.format(DIRECOTORY, key, itemInfo.getDirId());
                rawText = get(getChildUrl);
                if (rawText == null) {
                    //   key错误 或 没有对应事项
                    System.out.println("没有对应事项==============" + title);
                    crawlerCache.set("itemName", title);
                    crawlerCache.updateOfNull(title, key);
                    return;
                }
                List<ItemInfo> childRows = JSONArray.parseArray(rawText, ItemInfo.class);
                for (ItemInfo childRow : childRows) {
                    if (childRow.getEventName().equals(title)) {
                        detailUrl = String.format("http://%s.sczwfw.gov.cn/app/scworkguide/detail?id=%s&shardKey=%s&typeflag=3#",
                                area, childRow.getIdForStr(), childRow.getShardKey());
                        resultCopy.add(detailUrl);
                        map.put(detailUrl, itemInfo);
                        crawlerCache.saveCrawler(title, key, detailUrl, itemInfo);
                        notGetSuss = false;
                        return;
                    }
                }
                if (notGetSuss) {
                    //  目录里没有
                    System.out.println("未找到==============" + title);
                    crawlerCache.updateOfNull(title, key);
                }
            } else {
                detailUrl = String.format("http://%s.sczwfw.gov.cn/app/scworkguide/detail?id=%s&shardKey=%s&typeflag=3#",
                        area, itemInfo.getIdForStr(), itemInfo.getShardKey());
                resultCopy.add(detailUrl);
                map.put(detailUrl, itemInfo);
                crawlerCache.saveCrawler(title, key, detailUrl, itemInfo);
            }
        } else {
            //todo  未找到
            System.out.println("未找到==============" + title);
            crawlerCache.updateOfNull(title, key);
        }
    }


    private String post(Map<String, String> params) {
        try {
            return HttpClientUtil.doPost(site.getDomain(), params, headers());
        } catch (Exception e) {
            System.out.println("目录查找失败,重试中");
            return post(params);
        }
    }

    private String get(String getChildUrl) {
        try {
            return HttpClientUtil.doGet(getChildUrl);
        } catch (Exception e) {

            System.out.println("目录查找失败,重试中");
            return get(getChildUrl);
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



