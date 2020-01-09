package com.lidong.crawler.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 材料
 * @author 李东
 * @version 1.0
 * @date 2019/12/27 13:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Docs extends AcceptStyleOne {



    public void initDocs(){
        int rows = page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr").nodes().size() - 1;
        for (int j = 2; j <= rows; j++) {
            Docs doc =new Docs();
            //材料名称
            doc.setDocRequirement(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[1]/text()").toString());
            //来源渠道
            doc.setLssuingUnit(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[2]/text()").toString().trim());
            //来源渠道说明
            doc.setLssuingUnitRemark(page.getHtml().xpath("///*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[3]/text()").toString().trim());
            //材料类型
            doc.setGgPaperType(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[4]/text()").toString().trim());
            //材料形式
            doc.setDocType(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[5]/text()").toString().trim());
            //分数预约电话
            String num= page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[6]/text()").toString().trim();
            //纸质材料份数
            try {
                doc.setSubmitQty(num.substring(0, num.indexOf("份")));
            } catch (NullPointerException E) {
                doc.setSubmitQty("0");
            }
            //纸质材料规格
            doc.setGgStandard(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[7]/text()").toString().trim());
            //电子材料规格
            doc.setMaterialType(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[8]/text()").toString().trim());
            //材料必要性
            doc.setDocRequired(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[9]/text()").toString().trim());
            //示例样表
            String sl = "";
            if (page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[10]/span/@onclick").toString() == null) {
                sl = page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[10]/div/@onclick").toString();
            } else {
                sl = page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[10]/span/@onclick").toString();
            }
            String regex = "(?<=showDownFile\\().*?(?=\\))";
            if (StringUtils.isNotEmpty(sl)) {
                String[] link = getPatternStr(sl, regex).replace("\'", "").split(",");
                doc.setSlyb( String.format("http://mss.sczwfw.gov.cn/app/dataMaterialItem/getMaterialItemList?materialId=%s&type=2", link[0]));
            }
            String kb = "";
            if (page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[11]/span/@onclick").toString() == null) {
                kb = page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[11]/div/@onclick").toString();
            } else {
                kb = page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[11]/span/@onclick").toString();
            }
            //空白表格
            if (StringUtils.isNotEmpty(kb)) {
                String[] link = getPatternStr(kb, regex).replace("\'", "").split(",");
                doc.setKbbg(String.format("http://mss.sczwfw.gov.cn/app/dataMaterialItem/getMaterialItemList?materialId=%s&type=1", link[0]));
            }
            //需要提供材料的依据
            doc.setGgLawRemark(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[12]/@data-all").toString().trim());
            //受理标准
            doc.setStandard(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[13]/@data-all").toString().trim());
            //盖章或盖手印方式
            doc.setSign(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[14]/text()").toString().trim());
            //备注
            doc.setDocRemark(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + j + "]/td[15]/@data-all()").toString().trim());

            //填报须知
            doc.setNotice(page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[2]/table/tbody/tr[" + (rows + 1) + "]/td/text()").toString());
            docsList.add(doc);
        }
    }


    private String getPatternStr(String str, String pattern) {
        String mathstr = "";
        Pattern pt = Pattern.compile(pattern);
        Matcher match = pt.matcher(str);
        while (match.find()) {
            mathstr = match.group();
        }
        return mathstr;
    }

    /**
     * 材料名称
     */
    private String docRequirement;
    /**
     *来源渠道
     */
    private String lssuingUnit;
    /**
     *来源渠道说明
     */
    private String lssuingUnitRemark;
    /**
     *材料类型
     */
    private String ggPaperType;
    /**
     *材料形式
     */
    private String docType;
    /**
     *分数
     */
    private String submitQty;
    /**
     *纸质材料规格
     */
    private String ggStandard;
    /**
     *电子材料规格
     */
    private String materialType;
    /**
     *材料必要性
     */
    private String docRequired;
    /**
     *示例样表
     */
    private String slyb;
    /**
     * 空白表格
     */
    private String kbbg;

    /**
     *需要提供材料的依据
     */
    private String ggLawRemark;
    /**
     *受理标准
     */
    private String standard;
    /**
     *盖章或盖手印方式
     */
    private String Sign;
    /**
     *备注
     */
    private String docRemark;
    /**
     *填报须知
     */
    private String notice;

}
