package com.lidong.crawler.main;

import cn.wawi.common.spider2.Admin;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.OOSpider;

/**
 * 更新未更新的材料
 * @author 李东
 * @version 1.0
 * @date 2019/12/30 16:54
 */
public class updateDocs extends Admin {


    public static void main(String[] args) {
            OOSpider.create(links.getSite(), updateDocs.class)
                    .setIsExtractLinks(false)
                    .addUrl(links.findDocs().toArray(new String[0])).thread(11).run();
    }


    @Override
    public void afterProcess(Page page) {
        super.getItems(page, true, links.itemObj())
                .updateDocs();
    }

}
