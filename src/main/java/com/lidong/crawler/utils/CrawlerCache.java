package com.lidong.crawler.utils;


import cn.wawi.common.spider.entity.ItemInfo;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


/**
 * 持久化爬取链接到数据库
 * <p>
 * 李东
 * 2019.12.1
 */
public class CrawlerCache extends Model<CrawlerCache>
        implements Serializable {
    private static final long serialVersionUID = 7248569246763182397L;
    public static final CrawlerCache dao = new CrawlerCache();

    public static List<String> defaultLink(){
        return Db.query("SELECT link FROM ap_crawler_cache WHERE itemInfo IS NOT NULL");
    }
    public static List<CrawlerCache> findAll(){
        return dao.find("SELECT * FROM ap_crawler_cache WHERE itemInfo IS NOT NULL");
    }


    //保存天府新区事项
    void saveCrawler(String itemName, String areaCode, String link, ItemInfo itemInfo) {
        if (!"511411000000".equals(areaCode)) {
            saveElseCrawler(itemName, areaCode, link, itemInfo);
            return;
        }
        this.set("itemName", itemName);
        this.set("areaCode", areaCode);
        this.set("link", link);
        this.set("itemInfo", JSONObject.toJSON(itemInfo).toString());
        save();
    }

    //保存其他地区
    void saveElseCrawler(String itemName, String areaCode, String link, ItemInfo itemInfo) {
        Optional<CrawlerCache> name = Optional.ofNullable(dao.findFirst("select * from ap_crawler_cache where itemName = ?  ", itemName));
        this.set("itemName", itemName);
        this.set("areaCode", areaCode);
        this.set("link", link);
        this.set("itemInfo", JSONObject.toJSON(itemInfo).toString());
        name.ifPresent(crawlerCache -> {
            this.set("id", crawlerCache.get("id"));
            update();
        });
    }

    //找不到事项
    void updateOfNull(String itemName,String areaCode) {
        if (!"511411000000".equals(areaCode)) {
            updateElesOfNull(itemName);
            return;
        }

        this.set("itemName", itemName);
        this.set("areaCode", null);
        this.set("link", null);
        this.set("itemInfo", null);
        save();

    }

    //其他地区
    void updateElesOfNull(String itemName) {
        Optional<CrawlerCache> name = Optional.ofNullable(dao.findFirst("select * from ap_crawler_cache where itemName = ?  ", itemName));
        this.set("itemName", itemName);
        if (name.isPresent()) {
            this.set("id", name.get().get("id"));
            update();
        }
    }


}
