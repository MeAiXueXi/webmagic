package com.lidong.crawler.main;

import cn.wawi.common.spider2.Admin;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.OOSpider;

/**
 * 更新事项分类
 * @author 李东
 * @version 1.0
 * @date 2019/12/26 21:02
 */
public class updateItemType extends Admin {

    public static void main(String[] args) {
        links.defaultLink(x -> {
            OOSpider.create(links.getSite(), updateItemType.class)
                    .setIsExtractLinks(false)
                    .addUrl(x.toArray(new String[0])).thread(11).run();
        });
    }

    @Override
    public void afterProcess(Page page) {
        super.getItems(page, false, links.itemObj())
                .updateItemType();
    }

}
