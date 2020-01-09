package com.lidong.crawler.main;



import cn.wawi.common.spider2.utils.Config;
import cn.wawi.common.spider2.utils.UpdateItems;

/**
 * 更新事项的抓取地址
 *
 * @author 李东
 * @version 1.0
 * @date 2020/1/6 23:03
 */
public class updateItemWebLinks {
    static {
        Config.initPlugin();
    }
    public static void main(String[] args) {
        try {
            UpdateItems processor = new UpdateItems();

            /**
             *  第一个参数固定天府新区  511411000000=
             *    后面参数为替补
             *    ==
             */
            processor.updateLink(
                    "511411000000",
                    "510682000000"
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("出现异常停止");
        }
    }
}
