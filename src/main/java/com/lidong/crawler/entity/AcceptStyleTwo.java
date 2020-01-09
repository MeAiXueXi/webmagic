package com.lidong.crawler.entity;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;

/**
 *
 * 办理形式  窗口、网办 单独存在
 * @author 李东
 * @version 1.0
 * @date 2019/12/26 20:22
 */
@Data
public class AcceptStyleTwo {

    /**
     * 基本编码
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[12]/td[2]/text()")
    private String itemCode;
    /**
     * 实施编码
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[13]/td[2]/text()")
    private String useCode;
    /**
     * 事项类型
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[2]/td[2]/text()")
    private String xzType;
    /**
     * 行使层级
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[2]/td[4]/text()")
    private String xsType;
    /**
     * 事项来源
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[3]/td[2]/text()")
    private String itemSource;
    /**
     * 审查类型
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[3]/td[4]/text()")
    private String ggReviewAndStandards;
    /**
     * 业务办理项编码
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[13]/td[4]/text()")
    private String businessCode;
    /**
     * 权力来源
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[4]/td[2]/text()")
    private String ggSourceOfPower;
    /**
     * 实施主体
     *
     * AcceptDeptName，DeptName，DeptId
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[1]/td[2]/text()")
    private String DeptName;
    /**
     * 实施主体编码
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[12]/td[4]/text()")
    private String AcceptDeptCode;
    /**
     * 实施主体性质
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[4]/td[4]/text()")
    private String acceptDeptType;
    /**
     * 委托部门
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[9]/td[2]/text()")
    private String commissionedDept;
    /**
     * 联办机构
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[9]/td[4]/text()")
    private String unionAcceptOrgId;
    /**
     * 是否进驻政务大厅
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[5]/td[2]/text()")
    private String rzZwzx;
    /**
     * 是否纳入便民服务中心
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[5]/td[4]/text()")
    private String joinThisServiceCenter;
    /**
     * 行业
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[11]/td[4]/text()")
    private String industry;
    /**
     * 服务主题分类
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[11]/td[2]/text()")
    private String itemType;
    /**
     * 服务主题分类  字典
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[11]/td[2]/text()")
    private String itemTypeToDict;
    /**
     * 数量限制
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[10]/td[4]/text()")
    private String itemQtyLimit;
    /**
     * 是否支持网上支付
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[7]/td[4]/text()")
    private String onLinePay;
    /**
     * 认证等级需求
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[3]/td[2]/text()")
    private String attestation;
    /**
     * 事项版本
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[4]/table/tbody/tr[1]/td[4]")
    private String version;
    /**
     * 是否支持预约办理  窗口办理
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[2]/td[2]/text()")
    private String enabledOnlineOrder;
    /**
     * 是否支持物流快递  窗口办理
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[2]/td[4]/text()")
    private String logistics;
    /**
     * 必须现场办理原因说明  窗口办理
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[4]/text()")
    private String illustrate;
    /**
     * 是否支持全省就近取件  窗口办理
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[3]/td[2]/text()")
    private String pickUp;
    /**
     * 是否支持全省就近办理  窗口办理
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[3]/td[4]/text())")
    private String nearby;
    /**
     * 网办类型  网上办理
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[2]/text()")
    private String netHandleType;
    /**
     * 是否支持上门收取申请材料  网上办理
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[2]/td[2]/text()")
    private String collectMaterial;
    /**
     * 到办事现场次数  网上办理
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[4]/text()")
    private String onlinenNumberOfTimes;
    /**
     * 是否支持物流快递  网上办理
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[2]/td[4]/text()")
    private String onlinenlogistics;
    /**
     * 网上办理深度  网上办理
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr[3]/td[2]/text()")
    private String onlinenDeepness;
    /**
     * 咨询方式
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[3]/td[2]/text()")
    private String ggOnlineOrderTel;
    /**
     * 监督投诉方式
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[4]/td[2]/text()")
    private String SuperviseDesc;
    /**
     * 办理时间
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[1]/td[2]/text()")
    private String AcceptAddress;
    /**
     * 办理地点
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[3]/div[2]/div[1]/div[3]/table/tbody/tr[2]/td[2]/text()")
    private String AcceptWindow;
    /**
     * 到现场次数
     */
    @ExtractBy("//*[@id=\"app\"]/div/div[2]/div[2]/div[2]/div[1]/div[1]/div/span[1]/text()")
    private String numberOfTimes;
}
