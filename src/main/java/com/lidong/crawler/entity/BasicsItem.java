package com.lidong.crawler.entity;

import cn.wawi.utils.StringUtil;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.Data;
import lombok.Setter;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.Formatter;

import java.util.LinkedList;
import java.util.List;

/**
 * 基础信息
 * 页面无变化
 *
 * @author 李东
 * @version 1.0
 * @date 2019/12/25 23:37
 */
@Data
public class BasicsItem {
    protected Page page;
    protected List<ItemResultType> certs = new LinkedList<>();//证照
    protected List<Docs> docsList = new LinkedList<>();//材料
    protected List<Fee> feesList = new LinkedList<>();//收费信息
    protected List<Knowledge> knowledgeList = new LinkedList<>();//知识库
    protected List<Laws> lawsList = new LinkedList<>();//法律法规


    private String trafficDirections;
    private String runSystem;
    private String GgDealProgress;
    private String contactNumber;
    /**
     * 网页类型
     */
    @Setter
    @ExtractBy("//*[@id=\"app\"]/div/div[2]/div[1]/div[2]/span[2]/a[1]")
    @Formatter(subClazz = String.class)//test
    private String eventType;
    @Getter
    public Boolean getEventType() {
        try {
            if (StringUtil.isNullOrEmpty(this.eventType)) {
                return false;
            }
            String eventType = this.eventType.substring(this.eventType.indexOf("eventType&quot;:&quot;") + "eventType&quot;:&quot;".length()).substring(0, 2);
            if ("1A".equals(eventType) || "1Z".equals(eventType) || "2C".equals(eventType) ||
                    "2B".equals(eventType) || "1F".equals(eventType) || "1G".equals(eventType) ||
                    "2A".equals(eventType) || "1H".equals(eventType) || "1E".equals(eventType)) {
                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    /**
     * 事项编码
     */
    @ExtractBy("//*[@id='eventCode']/@value")
    private String itemKey;
    /**
     * 一体化 事项ID
     */
    @ExtractBy("//*[@id='implListId']/@value")
    private String implListId;
    /**
     * 实施对象
     */
    @ExtractBy("//*[@id=\"table1\"]/tbody/tr[1]/td[2]/text()")
    private String ggServerObjectInfo;
    /**
     * 服务对象
     */
    @ExtractBy("//*[@id=\"table1\"]/tbody/tr[1]/td[2]/text()")
    private String serverObject;
    /**
     * 办件类型
     */
    @ExtractBy("//*[@id=\"table1\"]/tbody/tr[1]/td[4]/text()")
    private String itemAcceptType;
    /**
     * 法定办结时限
     */
    @ExtractBy("//*[@id=\"table1\"]/tbody/tr[2]/td[2]/text()")
    private String legalDay;
    /**
     * 法定办结时限单位
     */
    @ExtractBy("//*[@id=\"table1\"]/tbody/tr[2]/td[4]/text()")
    private String legalDayUnit;
    /**
     * 承诺办结时限
     */
    @ExtractBy("//*[@id=\"table1\"]/tbody/tr[" + 3 + "]/td[2]/text()")
    private String dealDay;
    /**
     * 承诺办结时限单位
     */
    @ExtractBy("//*[@id=\"table1\"]/tbody/tr[3]/td[4]/text()")
    private String daelDayUnit;
    /**
     * 事项名称
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[2]/div[1]/div[1]/text()")
    private String title;
    /**
     * 审批结果  行数
     */
    @Setter
    @ExtractBy("//*[@id=\"table1\"]/tbody/tr[4]/td[1]")
    private String itemResultTypeRows;

    @Getter
    public Integer getItemResultTypeRows() {
        return Integer.parseInt(this.itemResultTypeRows.substring(this.itemResultTypeRows.indexOf("=") + 2, this.itemResultTypeRows.indexOf("=") + 3));
    }

}
