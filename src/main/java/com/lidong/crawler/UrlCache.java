package com.lidong.crawler;

import cn.wawi.common.spider.entity.ItemInfo;
import cn.wawi.common.spider2.utils.CrawlerCache;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import us.codecraft.webmagic.Site;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 缓存的网页链接、事项基本信息
 *
 * @author 李东
 * @version 1.0
 * @date 2019/12/28 19:20
 */
public interface UrlCache {

    Site getSite();

    default ConcurrentHashMap<String, ItemInfo> itemObj() {
        ConcurrentHashMap<String, ItemInfo> map = new ConcurrentHashMap<>(16);
        CrawlerCache.findAll().forEach(m -> map.put(m.get("link"), JSONObject.parseObject(m.get("itemInfo"), ItemInfo.class)));
        return map;
    }

    default Function<String, List<String>> consumeLink() {
        return Db::query;
    }

    //sql消费
    default void consumeLink(String sql, Consumer<List<String>> consumer) {
        consumer.accept(Db.query(sql));
    }

    default void defaultLink(Consumer<List<String>> consumer) {
        consumer.accept(CrawlerCache.defaultLink());
    }

}

