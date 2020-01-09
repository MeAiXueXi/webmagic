package com.lidong.crawler.main;

import cn.wawi.common.spider2.Admin;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.OOSpider;

/**
 * 更新法律法规
 * @author 李东
 * @version 1.0
 * @date 2019/12/30 21:18
 */
public class updateLaws extends Admin {

    public static void main(String[] args) {
        links.defaultLink(x -> {
            OOSpider.create(links.getSite(), updateLaws.class)
                    .setIsExtractLinks(false)
                    .addUrl(x.toArray(new String[0])).thread(11).run();
        });
    }

    @Override
    public void afterProcess(Page page) {
        super.getItems(page, true, links.itemObj())
              .updateLaws();
    }

}
