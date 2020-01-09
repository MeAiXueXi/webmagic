package com.lidong.crawler.utils;

import cn.wawi.model.sys.*;
import cn.wawi.model.sys.biz.*;
import cn.wawi.model.sys.sp.SpCertType;
import cn.wawi.model.sys.sp.SpCertTypeItem;
import cn.wawi.model.sys.sp.SpChargeItem;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

import java.io.File;

/**
 * @author 李东
 * @version 1.0
 * @date 2019/12/30 23:34
 */
public class Config {



    private static C3p0Plugin createC3p0Plugin() {
        return new C3p0Plugin(PropKit.get("url"),
                PropKit.get("username"),
                PropKit.get("password").trim(),
                PropKit.get("driver"));
    }

    public static void initPlugin() {
        PropKit.use(new File("/Users/lidong/Desktop/config/Seperate/jdbc.properties"));
        // 配置C3p0数据库连接池插件
        C3p0Plugin c3p0plugin = createC3p0Plugin();
        c3p0plugin.setAcquireIncrement(10);
        c3p0plugin.setInitialPoolSize(30);
        c3p0plugin.setMaxPoolSize(40);
        c3p0plugin.setMinPoolSize(30);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0plugin);
        arp.setShowSql(true);
        arp.addMapping("ap_publish_dict", "id", ApPublishDict.class);
        arp.addMapping("ap_publish_define", "id", ApPublishDefine.class);

        arp.addMapping("ap_publish_docs", "id", ApPublishDocs.class);

        arp.addMapping("ap_publish_docs_file", "id", ApPublishDocsFile.class);
        arp.addMapping("ap_publish_item", "id", ApPublishItem.class);

        arp.addMapping("sys_config", "id", cn.wawi.model.sys.Config.class);
        arp.addMapping("sys_file", "autoId", cn.wawi.model.sys.File.class);
        arp.addMapping("sys_user", "id", User.class);
        arp.addMapping("sys_dict", "id", Dict.class);

        arp.addMapping("sys_department", "id", Department.class);

        //收费项目
        arp.addMapping("sp_charge_item", "id", SpChargeItem.class);
        arp.addMapping("ap_publish_charge_item", ApPublishChargeItem.class);
        //爬虫缓存
        arp.addMapping("ap_crawler_cache", "id", CrawlerCache.class);
        arp.addMapping("ap_publish_laws", "id", ApPublishLaws.class);
        //分类
        arp.addMapping("ap_publish_dict", "id", ApPublishDict.class);

        //证照
        arp.addMapping("ap_cert_bind_log", "id", CertBindLog.class);
        arp.addMapping("sp_cert_type_item", "id", SpCertTypeItem.class);
        arp.addMapping("sp_cert_type", "id", SpCertType.class);

        c3p0plugin.start();
        arp.start();
    }
}
