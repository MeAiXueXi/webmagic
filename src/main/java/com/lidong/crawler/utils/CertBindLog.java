package com.lidong.crawler.utils;


import cn.wawi.common.spider2.entity.ItemResultType;
import cn.wawi.model.sys.biz.ApPublishItem;
import cn.wawi.model.sys.sp.SpCertType;
import cn.wawi.model.sys.sp.SpCertTypeItem;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * 证照绑定
 * 2019.12.7
 * by 李东
 */
public class CertBindLog extends Model<CertBindLog> {
    public static final CertBindLog dao = new CertBindLog();


    public void saveCert(ItemResultType itemResultType, ApPublishItem item,String url) {


        if(Db.queryInt("select  count(1)  from sp_cert_type_item where itemId = ?",item.getId())>0)
            return;


        String certName = itemResultType.getItemResultDesc();
        String cert = certName.contains("《") ? certName.substring(1, certName.length() - 1) : certName;
        List<SpCertType> spCertType = SpCertType.dao.find("select * from sp_cert_type where name  like CONCAT('%',?,'%' )", cert);
        SpCertTypeItem certTypeItem = new SpCertTypeItem();

        if (spCertType.size() == 0) {//木有证照
            this.set("itemName", item.getTitle().trim());
            this.set("link", url);
            this.set("ythCertName", itemResultType.getItemResultDesc().trim());
            this.set("certType", itemResultType.getItemResultId());
            this.save();
            this.set("id", null);
        } else {
            Db.tx(() -> {
                spCertType.forEach(x -> {

                    certTypeItem.setCertTypeId(x.getId());
                    certTypeItem.setItemId(item.getId());

                    this.set("itemName", item.getTitle().trim());
                    this.set("itemId", item.getId());
                    this.set("certName", x.getName());
                    this.set("certId", x.getId());
                    this.set("link", url);
                    this.set("ythCertName", itemResultType.getItemResultDesc().trim());
                    this.set("certType", itemResultType.getItemResultId());
                    this.set("status", 1);


                    this.save();
                    certTypeItem.save();
                    certTypeItem.setId(null);
                    this.set("id", null);

                });

                return true;
            });
        }
    }

}
