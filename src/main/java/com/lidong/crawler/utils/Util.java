package com.lidong.crawler.utils;

import cn.wawi.common.spider.ThemeProcesser;
import cn.wawi.common.spider2.Admin;
import cn.wawi.common.spider2.entity.Docs;
import cn.wawi.common.util.FileDown;
import cn.wawi.model.sys.Dict;
import cn.wawi.model.sys.File;
import cn.wawi.model.sys.biz.ApPublishDict;
import cn.wawi.model.sys.biz.ApPublishDocs;
import cn.wawi.model.sys.biz.ApPublishDocsFile;
import cn.wawi.model.sys.biz.ApPublishItem;
import cn.wawi.utils.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import us.codecraft.webmagic.Page;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 李东
 * @version 1.0
 * @date 2019/12/27 10:24
 */
public class Util {


    /**
     * 事项绑定分类
     */
    public void bindItemType(ApPublishItem apPublishItem, Admin admin) {
        ApPublishDict apPublishDict = new ApPublishDict();

        Db.tx(() -> {
            apPublishDict.set("itemId", apPublishItem.getId());
            if (apPublishItem.getGgServerObjectInfo()!=null&&apPublishItem.getGgServerObjectInfo().contains(Db.queryStr("select  id from sys_dict where name = '自然人';"))) {
                Arrays.stream(admin.getItemTypeToDict().trim().split(",")).forEach(x -> {
                    if (x.trim().equals("其他")) {
                        return;
                    }
                    Integer var1 = getPersonalDict(x);
                    if (var1 != null && Db.queryInt("select count(1) from ap_publish_dict where dictId =? and itemId = ?;", var1, apPublishItem.getId()) == 0) {
                        apPublishDict.setDictId(var1);
                        apPublishDict.save();
                        apPublishDict.setId(null);
                    }
                });
            }
            if (apPublishItem.getGgServerObjectInfo()!=null&&apPublishItem.getGgServerObjectInfo().contains(Db.queryStr("select id from sys_dict where name = '企业法人';"))) {
                Arrays.stream(admin.getItemTypeToDict().trim().split(","))
                        .forEach(x -> {
                            if (x.trim().equals("其他")) {
                                return;
                            }
                            Integer var1 = getEnterpriseDict(x);
                            if (var1 != null && Db.queryInt("select count(1) from ap_publish_dict where dictId =? and itemId = ?;", var1, apPublishItem.getId()) == 0) {
                                apPublishDict.setDictId(var1);
                                apPublishDict.save();
                                apPublishDict.setId(null);
                            }
                        });
            }
            return true;
        });

    }


    public Admin formatItem(Admin item) {
        item.setTitle(item.getTitle().trim());//去除左右空格
        //是否收费
        item.setFee("是".equals(item.getFee()) ? "yes" : "no");
        //跑路次数
        item.setNumberOfTimes(numberOfTimes(item.getNumberOfTimes().trim()));
        //权利来源
        item.setGgSourceOfPower(formatStr(item.getGgSourceOfPower().trim(), "sourceOfPower"));
        //行使层级
        item.setXsType(formatStr(item.getXsType().trim(), "2006"));
        //服务对象
        item.setGgServerObjectInfo(formatStr(item.getGgServerObjectInfo().trim(), "serverObject"));
        //服务对象
        StringBuilder sb = new StringBuilder();
        if (item.getServerObject().trim().contains("自然人")) {
            sb.append("NATURAL_MAN");
        }
        if (item.getServerObject().trim().contains("法人")) {
            sb.append("LEGAl_MAN");
        }
        item.setServerObject(sb.toString());
        //办理形式
        item.setAcceptStyle(transAcceptStyle(item.getAcceptStyle().trim()));
        //办件类型
        item.setItemAcceptType(formatStr(item.getItemAcceptType().trim(), "0002"));
        //通办范围
//        item.setItemSope(transItemScope(item.getItemSope()).toString());
        //承诺办结时限
        item.setDealDay(transDealDay(item.getDealDay().trim()).toString());
        //法定办结时限
        item.setLegalDay(transLegalDay(item.getLegalDay().trim()).toString());
        //是否预约
        item.setEnabledOnlineOrder(transEnableOnlineOrder(item.getEnabledOnlineOrder().trim()).toString());
        //事项来源
        item.setItemSource(formatStr(item.getItemSource().trim(), "scType"));
        //是否入驻政务中心
        item.setRzZwzx("是".equals(item.getRzZwzx()) ? "yes" : "no");
        //行业
        item.setIndustry(formatStr(item.getIndustry().trim(), "industry"));
        // 审查结果类型
        if (item.getCerts().size() > 0) {
            //取第一条类型
            item.setItemResultId(formatStr(item.getCerts().get(0).getItemResultId().trim(), "2009"));
        }
        //是否支持网上支付
        item.setOnLinePay(item.getOnLinePay().equals("无") ? "2" : "1");
        //网上办理--是否支持上门收取申请材料
        item.setCollectMaterial(item.getCollectMaterial().equals("是") ? "是" : "否");
        //认证等级需求
        item.setAttestation(formatStr(item.getAttestation().trim(), "attestation"));
        //窗口  --是否支持物流快递
        item.setLogistics("是".equals(item.getLogistics()) ? "1" : "2");
        //网上办理深度
        item.setOnlinenDeepness(formatStr(item.getOnlinenDeepness().trim(), "nlinetTansact"));
        //网上办理-是否支持物流快递
        item.setOnlinenlogistics("是".equals(item.getOnlinenlogistics()) ? "1" : "2");
        //网办类型
        item.setNetHandleType(formatStr(item.getNetHandleType().trim(), "netManageType"));
        //实施主体
        item.setAcceptDeptType(formatStr(item.getAcceptDeptType().trim(), "2007"));
        //事项类型
        item.setXzType(formatStr(item.getXzType(), "2005"));


        item.setTrafficDirections("四川省-眉山市-仁寿县-视高镇中建大道二段8号");
        item.setAcceptAddress("星期一至星期五 上午：09:00-12:00 下午：13:30-17:30 备注：法定节假日除外;");
        item.setAcceptWindow("四川省-眉山市-仁寿县-视高镇中建大道二段街道-8号,详细地址：（眉山天府新区党务政务服务中心2楼）;");
        item.setRunSystem("一窗式受理系统");
        item.setSuperviseDesc("12345");
        item.setGgDealProgress("10785,10786");
        item.setContactNumber("028-36333111");
        item.setItemType("0");
        return item;
    }

    private Integer getPersonalDict(String type) {
        return Db.queryInt("select id from sys_dict where name =? and parentId =266 ", type.trim());
    }


    private Integer getEnterpriseDict(String type) {
        return Db.queryInt("select id from sys_dict where name =? and parentId =264 ", type.trim());
    }


    /**
     * 通用的字典查询
     *
     * @param dictName
     * @param groupName
     * @return str
     */
    public String formatStr(String dictName, final String groupName) {
        try {
            if (StringUtil.isNullOrEmpty(dictName)) {
                return null;
            }
            Arrays.stream(dictName.split(","))
                    .map(x -> x.equals("a4") ? "A4纸" : x)
                    .map(x -> x.equals("A4") ? "A4纸" : x)
                    .map(x -> x.equals("证照") ? "相关证件" : x.trim()).forEach(x -> {
                        if(Dict.findByNameAndGroup(x, groupName)==null){
                            int parentId=Db.queryInt("select parentId from sys_dict where  groupcode =? and parentId !=40   limit 0,1;",groupName);
                            Db.update("INSERT  INTO sys_dict(name,type,description,sort,status,groupcode,parentId,fileId,isDelete,childQty) values (?,'numberOfTimes',?,1,1,?,?,0,0,0);",x,x,groupName,parentId);
                            Db.update("UPDATE sys_dict SET  value = id WHERE  name = ? and groupcode = ? ",x,groupName);
                        }
                    }
            );
            return Arrays.stream(dictName.split(","))
                    .map(x -> x.equals("a4") ? "A4纸" : x)
                    .map(x -> x.equals("A4") ? "A4纸" : x)
                    .map(x -> x.equals("证照") ? "相关证件" : x)
                    .map(x -> StringUtils.isNotEmpty(x) ? Dict.findByNameAndGroup(x.trim(), groupName).getId().toString() : x)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.joining(","));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 跑路次数
     */
    public String numberOfTimes(String var) {
        if(StringUtil.isNullOrEmpty(var)){
            return Db.queryStr("select id from sys_dict where name ='零跑路'");
        }
        String dictId;
        if ("0".equals(var)) {
            dictId = Db.queryStr("select id from sys_dict where name ='零跑路'");
        } else if ("1".equals(var)) {
            dictId = Db.queryStr("select id from sys_dict where name ='一次跑路'");
        } else if ("2".equals(var)) {
            dictId = Db.queryStr("select id from sys_dict where name ='二次跑路'");
        } else {
            dictId = Db.queryStr("select id from sys_dict where name ='两次以上跑路'");
        }
        return dictId;
    }

    /**
     * 办理形式
     */
    public String transAcceptStyle(String name) {
        try {
            if (StringUtil.isNullOrEmpty(name)) {
                return null;
            }
            return Arrays.stream(name.split(","))
                    .map(x -> x.equals("网上申请") ? "ONLINE_POST" : x)
                    .map(x -> x.equals("现场申请") ? "WINDOW_POST" : x)
                    .map(x -> x.equals("邮递申请") ? "PRETRIAL_POST" : x)
                    .map(x -> x.equals("窗口办理") ? "WINDOW_POST" : x)
                    .map(x -> x.equals("网上办理") ? "ONLINE_POST" : x)
                    .map(x -> x.equals("网上预审后窗口办理") ? "PRETRIAL_POST" : x)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.joining(","));
        } catch (Exception ex) {
            return null;
        }

    }

    /**
     * 承诺办结时限
     */
    public Integer transDealDay(String name) {

        if (StringUtil.isNullOrEmpty(name)) {
            return 0;
        }
        try {
            return Integer.parseInt(name);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 法定办结时限
     */
    public Integer transLegalDay(String name) {
        if (StringUtil.isNullOrEmpty(name)) {
            return 0;
        }
        try {
            return Integer.parseInt(name.replace("个月", "").replace("日", "").trim());
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 是否预约
     */
    public Integer transEnableOnlineOrder(String sfyy) {
        if (StringUtil.isNullOrEmpty(sfyy)) {
            return 0;
        }
        try {
            if ("是".equals(sfyy.trim())) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 通办范围
     */
    public Integer transItemScope(String name) {
        try {
            if (StringUtil.isNullOrEmpty(name)) {
                return null;
            }
            if ("无".equals(name.trim())) {
                return Dict.findByNameAndGroup("不可通办", "2008").getId();
            }

            return Dict.findByNameAndGroup(name, "2008").getId();
        } catch (Exception ex) {
            return null;
        }
    }
    /**
     * 材料类型
     */
    public String transDocTypes(String nameZz) {
        String[] var1 = nameZz.split("和");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < var1.length; i++) {
            sb.append(var1[i]).append(",");
        }
        return formatStr(sb.substring(0, sb.length() - 1),"docType");
    }
    /**
     * 材料必要性
     */
    public String transDocRequired(String name) {
        if (name.startsWith("必要")) {
            return "1";
        } else if (name.startsWith("非必要")) {
            return "2";
        }
        return "1";
    }

    public ApPublishDocs formatDocs(Docs docs){
        ApPublishDocs apPublishDocs = new ApPublishDocs();
        //名称
        apPublishDocs.setDocRequirement(docs.getDocRequirement().trim());
        //来源渠道
        apPublishDocs.setLssuingUnit(docs.getLssuingUnit());
        //来源渠道说明
        apPublishDocs.setLssuingUnitRemark(docs.getLssuingUnitRemark());
        //材料类型
        apPublishDocs.setGgPaperType(transDocTypes(docs.getGgPaperType()));
        //材料形式
        apPublishDocs.setDocType(formatStr(docs.getDocType(),"1001"));
        //纸质材料份数
        apPublishDocs.setSubmitQty(Integer.parseInt(docs.getSubmitQty().trim()));
        //纸质材料规格
        apPublishDocs.setGgStandard(docs.getGgStandard());
        //电子材料规格
        apPublishDocs.setMaterialType(formatStr(docs.getMaterialType(),"docStandard"));

        //材料必要性
        apPublishDocs.setDocRequired(transDocRequired(docs.getDocRequired()));
        //依据
        apPublishDocs.setGgLawRemark(docs.getGgLawRemark());

        //受理标准
        apPublishDocs.setStandard(docs.getStandard());
//        签章要求
        apPublishDocs.setSign(formatStr(docs.getSign(),"docSign"));
        //备注  受理标准
        apPublishDocs.setDocRemark(docs.getDocRemark());
        //填报须知
        apPublishDocs.setNotice(docs.getNotice());
        return apPublishDocs;
    }

    /**
     * 保存样表
     * @param docfile  文件
     * @param tempApItem  事项
     * @param docs
     * @param type
     * @param attachmentsIdString
     */
    private void saveApPublishDocsFile(File docfile, ApPublishItem tempApItem, ApPublishDocs docs, int type, String attachmentsIdString) {
        //保存样表
        if (docfile != null) {

            ApPublishDocsFile docsFile = ApPublishDocsFile.findBydocIdandFileId(tempApItem.getId(), docfile.getId());
            if (docsFile == null) {
                ApPublishDocsFile apPublishDocsFile = new ApPublishDocsFile();
                apPublishDocsFile.setItemId(tempApItem.getId());
                apPublishDocsFile.setDocId(docs.getId());
                apPublishDocsFile.setStatus(1);
                apPublishDocsFile.setAddTime(new Date());
                apPublishDocsFile.setModifyTime(new Date());
                apPublishDocsFile.setFileId(docfile.getId());
                apPublishDocsFile.setName(docfile.getDocName());
                apPublishDocsFile.setAttachmentsIdString(attachmentsIdString);
                //空表为1样表为2 4.0空表样表为一个字段
                apPublishDocsFile.setType(type);
                apPublishDocsFile.save();
            } else {
                docsFile.setModifyTime(new Date());
                docsFile.update();
            }
        }
    }


    /**
     * 示例样表下载
     *
     * @param page
     */
    public void doDownFileInfo(Page page) {


        String url = page.getUrl().toString();
        MultiValueMap<String, String> queryParams =
                UriComponentsBuilder.fromUriString(url).build().getQueryParams();
        String docsId = queryParams.getFirst("docsId");
        String itemKey = queryParams.getFirst("itemKey");
        String itemId = queryParams.getFirst("itemId");
        String type = queryParams.getFirst("type");
        ApPublishDocs doc = ApPublishDocs.dao.findById(docsId);
        ApPublishItem tempApItem = ApPublishItem.dao.findById(itemId);
        String result = page.getRawText();
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!"success".equals(jsonObject.getString("result"))) {
            return;
        }
        JSONArray rows = jsonObject.getJSONArray("rows");
        if (rows.size() <= 0) {
            return;
        }
        for (int i = 0; i < rows.size(); i++) {
            JSONObject downJson = rows.getJSONObject(i);

            if (downJson.getString("attachSafe") == null || downJson.getString("attachSafe").length() <= 0) {
                break;
            }

            String downUrl = String.format("http://mss.sczwfw.gov.cn/app/attachments/downloadFile?id=%s", URLEncoder.encode(downJson.getString("attachSafe")));
            String ybxz = "";
            if (!StringUtil.isNullOrEmpty(downUrl)) {
                System.out.println("下载地址：" + downUrl);
                String savePath = ThemeProcesser.baseSavePath + itemKey;

                synchronized (this) {
                    ybxz = FileDown.downLoadFromUrl(downUrl, savePath, docsId + "_");
                }

                if (!StringUtils.isNotEmpty(ybxz)) {
                    System.out.println("下载失败，重试中");
                    doDownFileInfo(page);
                }
                System.out.println("获取材料：："+tempApItem.getTitle());
                if (StringUtils.isNotEmpty(ybxz)) {
                    java.io.File file = new java.io.File(ybxz);
                    if (file.exists()) {
                        Db.tx(() -> {
                            boolean flag = true;
                            try {

                                ApPublishDocsFile docsFile = ApPublishDocsFile.dao.findFirst("select * from ap_publish_docs_file where attachmentsIdString=? limit 0,1", downJson.getString("attachmentsIdString"));
                                if (docsFile == null) {
                                    File sysFile = File.createUploadFile("ApPublishDocs", file);
                                    assert type != null;
                                    saveApPublishDocsFile(sysFile, tempApItem, doc, Integer.parseInt(type), downJson.getString("attachmentsIdString"));
                                } else {
                                    //关联已存在材料
                                    ApPublishDocsFile newDocs = new ApPublishDocsFile();
                                    newDocs.setItemId(Integer.parseInt(Objects.requireNonNull(itemId)));
                                    newDocs.setDocId(doc.getId());
                                    newDocs.setStatus(1);
                                    newDocs.setFileId(docsFile.getFileId());
                                    newDocs.setName(docsFile.getName());
                                    newDocs.setAddTime(new Date());
                                    newDocs.setModifyTime(new Date());
                                    newDocs.setType(Integer.parseInt(Objects.requireNonNull(type)));
                                    newDocs.setAttachmentsIdString(docsFile.getAttachmentsIdString());
                                    newDocs.save();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("error ----  ：" + tempApItem.getTitle());
                                flag = false;
                            }
                            return flag;
                        });
                    }
                }


            }
        }

    }
}
