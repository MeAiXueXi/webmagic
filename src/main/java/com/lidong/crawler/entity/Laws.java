package com.lidong.crawler.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 法律法规
 * @author 李东
 * @version 1.0
 * @date 2019/12/27 22:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Laws extends Knowledge {
    protected void  initLaws(){
        int rowslyj = page.getHtml().xpath("//*[@id=\"table2\"]/tbody").nodes().size();
        if (rowslyj >= 1) {
            for (int yj = 1; yj <= rowslyj; yj++) {
                Laws laws = new Laws();
                laws.setLegalName(page.getHtml().xpath("//*[@id=\"table2\"]/tbody[" + yj + "]/tr[1]/td[3]/text()").toString());
                laws.setRegulationsType(page.getHtml().xpath("//*[@id=\"table2\"]/tbody[" + yj + "]/tr[2]/td[2]/text()").toString());
                laws.setLawRemark(page.getHtml().xpath("//*[@id=\"table2\"]/tbody[" + yj + "]/tr[3]/td[2]/text()").toString());
                laws.setLawDesc(page.getHtml().xpath("//*[@id=\"table2\"]/tbody[" + yj + "]/tr[4]/td[2]/text()").toString());
                lawsList.add(laws);
            }
        }
    }


    /**
     * 法律法规名称
     */
    private String legalName;
    /**
     * 法律法规类型
     */
    private String regulationsType;
    /**
     * 条款
     */
    private String lawRemark;
    /**
     * 条款内容
     */
    private String lawDesc;

}
