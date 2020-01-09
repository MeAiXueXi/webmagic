package com.lidong.crawler.entity;

import cn.wawi.utils.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批结果类型
 * <p>
 *
 * @author 李东
 * @version 1.0
 * @date 2019/12/26 00:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemResultType extends BasicsItem {


    protected void initItemResultType() {
        //😁😁
        Integer rows = super.getItemResultTypeRows();
        for (int jgRow = 1; jgRow < rows; jgRow++) {
            ItemResultType itemResultType =new ItemResultType();
            itemResultType.setItemResultId(page.getHtml().xpath("//*[@id=\"table1\"]/tbody/tr[" + (jgRow + 4) + "]/td[1]/text()").toString());
            if (!StringUtil.isNullOrEmpty(itemResultType.getItemResultId()) &&("证照".equals(itemResultType.getItemResultId().trim())||"批文".equals(itemResultType.getItemResultId().trim()))) {
                itemResultType.setItemResultDesc(page.getHtml().xpath("//*[@id=\"table1\"]/tbody/tr[" + (jgRow + 4) + "]/td[2]/text()").toString());
                itemResultType.setItemResultSample(page.getHtml().xpath("//*[@id=\"table1\"]/tbody/tr[" + (jgRow + 4) + "]/td[3]/span/@onclick").toString());
                super.certs.add(itemResultType);
            }
        }
        fee = page.getHtml().xpath("//*[@id=\"table1\"]/tbody/tr[" + (rows + 4) + "]/td[2]/text()").toString();
        acceptStyle = page.getHtml().xpath("//*[@id=\"table1\"]/tbody/tr[" + (rows + 4) + "]/td[4]/text()").toString();
        itemSope  = page.getHtml().xpath("//*[@id=\"table1\"]/tbody/tr[" + (rows + 5) + "]/td[2]/text()").toString();
        specialProcess = page.getHtml().xpath("//*[@id=\"table1\"]/tbody/tr[" + (rows + 5) + "]/td[4]/text()").toString();
    }
    /**
     * 结果类型
     */
    private String itemResultId;
    /**
     * 审批结果名称
     */
    private String itemResultDesc;
    /**
     * 审批结果样本
     * todo 这是一个下载链接 可以下载证照
     */
    private String itemResultSample;
    /**
     * 是否收费
     */
    private String fee;
    /**
     * 办理形式
     */
    private String acceptStyle;
    /**
     * 通办范围
     */
    private String itemSope;
    /**
     * 特别程序
     */
    private String specialProcess;


}
