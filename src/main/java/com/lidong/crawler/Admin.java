package com.lidong.crawler;

import cn.wawi.common.spider.entity.ItemInfo;
import cn.wawi.common.spider2.entity.*;
import cn.wawi.common.spider2.utils.*;
import cn.wawi.common.spider2.utils.Config;
import cn.wawi.model.sys.*;
import cn.wawi.model.sys.biz.*;
import cn.wawi.model.sys.sp.SpChargeItem;
import com.jfinal.plugin.activerecord.Db;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author 李东
 * @version 1.0
 * @date 2019/12/27 12:55
 */
public abstract class Admin
        extends Laws
        implements AfterExtractor, Serializable {

    private static final long serialVersionUID = 1248569246763182397L;

    public   volatile ConcurrentHashMap<String, ItemInfo> map = new ConcurrentHashMap<>(16);

    volatile ConcurrentHashMap<String, Integer> parentItemMap = new ConcurrentHashMap<>(16);

    ApPublishItem item = new ApPublishItem();

    public static FindCacheLinks links = new FindCacheLinks();

    Util itemUtil =new Util();

    static {
        Config.initPlugin();
    }

    /**
     * 初始化数据
     *
     * @param page      网页信息
     * @param doNotSave 是否保存事项      如果是第二次更新 不建议保存事项信息，会覆盖掉之前的数据
     * @param map       事项缓存
     * @return
     */
    public final Admin getItems(Page page, Boolean doNotSave, ConcurrentHashMap<String, ItemInfo> map) {
        if (getEventType()) {
            super.page = page;
            this.map = map;
            super.initItemResultType();
            this.formatXzType();
            BeanUtils.copyProperties(itemUtil.formatItem(this), item);
            this.saveItem(doNotSave);
        }
        return this;
    }



    public Admin  bindCert(){
        CertBindLog certBindLog =new CertBindLog();
        if(getEventType()){
         for (ItemResultType res:certs){
             certBindLog.saveCert(res,item,this.page.getUrl().toString());
         }
        }
        return this;
    }


    /**
     * 下载材料
     *
     * @return
     */
    public  Admin updateDocs() {
        if (getEventType()) {
            super.initDocs();
            //材料信息
            for (Docs cl :
                    docsList) {
                ApPublishDocs doc = itemUtil.formatDocs(cl);
                doc.setItemId(item.getId());

                ApPublishDocs tem = ApPublishDocs.dao.findFirst("SELECT * FROM ap_publish_docs WHERE  itemId=? and docRequirement =? and enabled = true", item.getId(), doc.getDocRequirement().trim());
                //没有这个材料
                if (tem == null) {
                    doc.setEnabled(true);
                    doc.setModifyTime(new Date());
                    doc.setAddTime(new Date());
                    doc.save();
                    doDownFile(doc.getId(),item.getId(),cl.getSlyb(),cl.getKbbg());
                }else {
                    if(ApPublishDocs.dao.findFirst("select * from ap_publish_docs_file where docId = ?",tem.getId()) == null){
                        doDownFile(tem.getId(),item.getId(),cl.getSlyb(),cl.getKbbg());
                    }
                }
            }
        }
        return this;
    }



    private  void doDownFile(Integer docId,Integer itemId,String slyb,String kbbg){
        if (StringUtils.isNotEmpty(slyb)) {
            String url = slyb + "&docsId=" + docId + "&itemId=" + itemId;
            downFile(url);
        }
        if (StringUtils.isNotEmpty(kbbg)) {
            String url = kbbg + "&docsId=" + docId + "&itemId=" + itemId;
            downFile(url);
        }
    }

    /**
     * 下载文件
     *
     * @param url 文件访问地址
     */
    private void downFile(String url) {
        Spider.create(new PageProcessor() {
            @Override
            public void process(Page page) {
                itemUtil.doDownFileInfo(page);
            }

            @Override
            public Site getSite() {
                return new Site().setCycleRetryTimes(2000).setTimeOut(2000);
            }
        }).addUrl(url).run();
    }

    public final Admin updateKnowledge() {
        if (getEventType()) {
            super.initKnowledge();
        }
        return this;
    }

    public final Admin updateFee() {
        if (getEventType()) {
            super.initFee();
            for (Fee fe : feesList
            ) {
                SpChargeItem spChargeItem = new SpChargeItem();
                ApPublishChargeItem apPublishChargeItem = new ApPublishChargeItem();
                apPublishChargeItem.setVersion(item.getVersion());
                apPublishChargeItem.setItemId(item.getId());
                apPublishChargeItem.setChargeId(spChargeItem.getId());
                BeanUtils.copyProperties(fe, spChargeItem);
                spChargeItem.setChargeNo("C" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
                Optional<Integer> spId = Optional.ofNullable(Db.queryInt("select a.id from sp_charge_item a,ap_publish_charge_item b where a.id=b.chargeId and  a.chargeName = ?", spChargeItem.getChargeName()));
                Db.tx(() -> {
                    if (spId.isPresent()) {
                        spChargeItem.setId(spId.get());
                        apPublishChargeItem.setId(Db.queryInt("select id from ap_publish_charge_item where chargeId = ?", spId.get()));
                        spChargeItem.update();
                        apPublishChargeItem.update();
                    } else {
                        spChargeItem.save();
                        apPublishChargeItem.save();
                        spChargeItem.setId(null);
                        apPublishChargeItem.setId(null);
                    }
                    return true;
                });
            }
        }
        return this;
    }

    public final Admin updateLaws() {
        if (getEventType()) {
            super.initLaws();
            for (Laws law :
                    lawsList
            ) {
                ApPublishLaws apPublishLaws = new ApPublishLaws();
                BeanUtils.copyProperties(law, apPublishLaws);
                apPublishLaws.setItemId(item.getId());
                apPublishLaws.setEnabled(true);
                ApPublishLaws yjSize = ApPublishLaws.dao.findFirst("select  * from ap_publish_laws where legalName =? and itemid = ? ", apPublishLaws.getLegalName(), item.getId());

                if (yjSize == null) {
                    apPublishLaws.save();
                } else {
                    apPublishLaws.setId(yjSize.getId());
                    apPublishLaws.update();
                }
            }
        }
        return this;
    }


    private void saveItem(Boolean doNotSave) {
        if (getEventType()) {
            Db.tx(() -> {
                ApPublishItem tempApItem = ApPublishItem.findByItemTitleNoParent(item.getTitle());
                if (doNotSave) {
                    item = tempApItem;
                    return true;
                }
                Integer prentId = processParentId(page);
                if (tempApItem == null) {
                    item.setModifyTime(new Date());
                    item.setAddTime(new Date());
                    item.setParentId(prentId);
                    if (prentId != 0) {
                        ApPublishItem parent = ApPublishItem.dao.findById(prentId);
                        item.setPointFlowId(parent.getPointFlowId());
                        item.setDeptId(parent.getDeptId());
                        item.setAcceptDeptId(parent.getAcceptDeptId());
                        item.setAcceptDeptName(parent.getAcceptDeptName());
                        item.setDeptName(parent.getDeptName());
                    }
                    item.setItemSope(10599);
                    item.save();
                } else {
                    item.setItemSope(10599);
                    //更新事项
//            tempApItem.update();
                    item = tempApItem;
                }
                return true;
            });
        }
    }


    /**
     * 格式化 不同的办理形式
     */
    private void formatXzType() {
        String style = super.getAcceptStyle();
        if (getEventType() && ("窗口办理".equals(style) || "网上办理".equals(style))) {
            OOSpider.create(Site.me(), (PageModelPipeline<AcceptStyleTwo>) (o, task) -> BeanUtils.copyProperties(o, this), AcceptStyleTwo.class)
                    .setIsExtractLinks(false)
                    .addUrl(String.valueOf(page.getUrl())).run();
        }
        if (getEventType() && "无".equals(style)) {
            OOSpider.create(Site.me(), (PageModelPipeline<NotHaveAcceptSytle>) (o, task) -> BeanUtils.copyProperties(o, this), NotHaveAcceptSytle.class)
                    .setIsExtractLinks(false)
                    .addUrl(String.valueOf(page.getUrl())).run();
        }
    }

    /**
     * 绑定分类
     *
     * @return
     */
    public final Admin updateItemType() {
        if (getEventType()) {
            itemUtil.bindItemType(item, this);
        }
        return this;
    }


    private synchronized Integer processParentId(Page page) {
        Integer parentId = 0;
        if (map.containsKey(page.getUrl().toString())) {
            ItemInfo itemInfo = map.get(page.getUrl().toString());
            if (parentItemMap.containsKey(itemInfo.getDirIdForStr())) {
                parentId = parentItemMap.get(itemInfo.getDirIdForStr());
            } else {
                ApPublishItem dirItem = new ApPublishItem();

                ApPublishItem item = ApPublishItem.dao.findFirst("select * from ap_publish_item where title = ? and childQty = 0", itemInfo.getEventName());
                //事项已存在 
                if (item != null) {
                    //事项本身无目录
                    if (!Optional.ofNullable(itemInfo.getDirName()).isPresent()) {
                        parentId = 0;
                    } else {

                        ApPublishItem var1 = ApPublishItem.dao.findFirst("select * from ap_publish_item where title = ?", itemInfo.getDirName());

                        //现有目录正确
                        if (var1 != null && itemInfo.getDirName().equals(var1.getTitle())) {
                            parentId = var1.getId();
                        } else {

                            Integer parId = Db.queryInt("select id from ap_publish_item where title = ? and childQty != 0", itemInfo.getDirName());
                            if (parId != null) {
                                //移动到正确目录
                                parentId = parId;
                            } else {
                                //创建父目录
                                dirItem.setTitle(itemInfo.getDirName());
                                dirItem.setParentId(0);
                                dirItem.setStatus("1");
                                dirItem.save();
                                parentId = dirItem.getId();
                                parentItemMap.put(itemInfo.getDirIdForStr(), parentId);
                            }
                        }
                    }
                }
                //无对应事项
                if (item == null) {
                    if (0 != Integer.parseInt(itemInfo.getDirId())) {
                        Integer parId = Db.queryInt("select id from ap_publish_item where title = ? and childQty != 0", itemInfo.getDirName());
                        //事项父目录存在

                        if (parId != null) {
                            parentId = parId;
                        } else {
                            //创建父目录
                            dirItem.setTitle(StringUtils.isEmpty(itemInfo.getDirName()) ? itemInfo.getEventName() : itemInfo.getDirName());
                            dirItem.setParentId(0);
                            dirItem.save();
                            parentId = dirItem.getId();
                            parentItemMap.put(itemInfo.getDirIdForStr(), parentId);
                        }
                    } else {
                        parentId = 0;
                    }
                }
            }
        }
        return parentId;
    }


}
