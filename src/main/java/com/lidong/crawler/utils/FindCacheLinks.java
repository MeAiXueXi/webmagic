package com.lidong.crawler.utils;

import cn.wawi.common.spider2.UrlCache;
import us.codecraft.webmagic.Site;

import java.util.List;


/**
 * @author 李东
 * @version 1.0
 * @date 2019/12/28 19:17
 */
public class FindCacheLinks implements UrlCache {

    public List<String> findDocs(){
      return consumeLink().apply("select b.link\n" +
              "from ap_publish_item a,ap_crawler_cache b  where  a.id not in (select d.itemId\n" +
              "from ap_publish_docs d ,ap_publish_docs_file e where d.id =e.docId and attachmentsIdString is not null group by d.itemId )\n" +
              "and childQty =0 and a.title = b.itemName and b.areaCode is not  null ;");
    }

    @Override
    public Site getSite() {
        return  new  Site().setCycleRetryTimes(2000).setTimeOut(2000);
    }
}
