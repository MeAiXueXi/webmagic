package com.lidong.crawler.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收费信息
 *
 * @author 李东
 * @version 1.0
 * @date 2019/12/27 15:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Fee extends Docs {


    protected void initFee() {
        if ("收费".equals(super.getFee())) {
            int rowsf = page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[5]/table/tbody/tr").nodes().size() + 1;
            for (int tr = 2; tr < rowsf; tr++) {
                Fee fee = new Fee();
                fee.setChargeName(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[5]/table/tbody/tr[" + tr + "]/td[1]/text()").toString().trim());
                fee.setChargeStandard(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[5]/table/tbody/tr[" + tr + "]/td[2]/text()").toString().trim());
                fee.setChargeAccording(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[5]/table/tbody/tr[" + tr + "]/td[3]/text()").toString().trim());
                fee.setExempt(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[5]/table/tbody/tr[" + tr + "]/td[4]/text()").toString().trim());
                fee.setExemptAccording(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[5]/table/tbody/tr[" + tr + "]/td[5]/text()").toString().trim());
                fee.setRemark(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[5]/table/tbody/tr[" + tr + "]/td[6]/text()").toString().trim());
                feesList.add(fee);
            }
        }
    }

    /**
     * 收费项目名称
     */
    private String chargeName;

    /**
     * 收费标准
     */
    private String chargeStandard;

    /**
     * 收费依据
     */
    private String chargeAccording;

    /**
     * 是否允许减免
     */
    private String exempt;

    /**
     * 允许减免依据
     */
    private String exemptAccording;

    /**
     * 备注
     */
    private String remark;


}
