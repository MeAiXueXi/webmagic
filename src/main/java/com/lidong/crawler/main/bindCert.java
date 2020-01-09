package com.lidong.crawler.main;

import cn.wawi.common.spider2.Admin;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.OOSpider;

/**
 *
 *
 * 绑定证照
 * 未实现下载证照
 * @author 李东
 * @version 1.0
 * @date 2020/1/4 17:56
 */
public class bindCert extends Admin {


    public static void main(String[] args) {
        links.defaultLink(x -> {
            OOSpider.create(links.getSite(), bindCert.class)
                    .setIsExtractLinks(false)
                    .addUrl(x.toArray(new String[0])).thread(11).run();
        });
    }

    @Override
    public void afterProcess(Page page) {
        super.getItems(page, true, links.itemObj())
                .bindCert();
    }
}
