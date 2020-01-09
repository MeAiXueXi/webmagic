package com.lidong.crawler.entity;

import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.Data;
import lombok.Setter;
import us.codecraft.webmagic.model.annotation.ExtractBy;

/**
 *  没有办理形式
 * @author
 * @version 1.0s
 * @date 2019/12/29 10:27
 */
@Data
public class NotHaveAcceptSytle {
    /**
     * 基本编码
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[12]/td[2]/text()")
    private String itemCode;
    /**
     * 实施编码
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[13]/td[2]/text()")
    private String useCode;
    /**
     * 事项类型
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[2]/td[2]/text()")
    private String xzType;
    /**
     * 行使层级
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[2]/td[4]/text()")
    private String xsType;
    /**
     * 事项来源
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[3]/td[2]/text()")
    private String itemSource;
    /**
     * 审查类型
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[3]/td[4]/text()")
    private String ggReviewAndStandards;
    /**
     * 业务办理项编码
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[13]/td[4]/text()")
    private String businessCode;
    /**
     * 权力来源
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[4]/td[2]/text()")
    private String ggSourceOfPower;
    /**
     * 实施主体
     * <p>
     * AcceptDeptName，DeptName，DeptId
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[1]/td[2]/text()")
    private String DeptName;
    /**
     * 实施主体编码
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[12]/td[4]/text()")
    private String AcceptDeptCode;
    /**
     * 实施主体性质
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[4]/td[4]/text()")
    private String acceptDeptType;
    /**
     * 委托部门
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[9]/td[2]/text()")
    private String commissionedDept;
    /**
     * 联办机构
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[9]/td[4]/text()")
    private String unionAcceptOrgId;
    /**
     * 是否进驻政务大厅
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[5]/td[2]/text()")
    private String rzZwzx;
    /**
     * 是否纳入便民服务中心
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[5]/td[4]/text()")
    private String joinThisServiceCenter;
    /**
     * 行业
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[11]/td[4]/text()")
    private String industry;
    /**
     * 服务主题分类
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[11]/td[2]/text()")
    private String itemType;
    /**
     * 服务主题分类  字典
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[11]/td[2]/text()")
    private String itemTypeToDict;
    /**
     * 数量限制
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[10]/td[4]/text()")
    private String itemQtyLimit;
    /**
     * 是否支持网上支付
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[7]/td[4]/text()")
    private String onLinePay;

    /**
     * 事项版本
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[1]/td[4]/text()")
    private String version;
    /**
     * 咨询方式
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[3]/td[2]/text()")
    private String ggOnlineOrderTel;
    /**
     * 监督投诉方式
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[4]/td[2]/text()")
    private String SuperviseDesc;
    /**
     * 办理时间
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[2]/text()")
    private String AcceptAddress;
    /**
     * 办理地点
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[2]/td[2]/text()")
    private String AcceptWindow;
    @Setter
    private String numberOfTimes;
    @Getter
    public String getNumberOfTimes(){
        return "0";
    }
}
