package com.lidong.crawler.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * 知识库
 * @author 李东
 * @version 1.0
 * @date 2019/12/27 22:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Knowledge extends Fee {

    protected  void initKnowledge(){
        int rowwt = page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[8]/div").nodes().size();
        if (rowwt > 1) {
            for (int j = 2; j <= rowwt; j++) {
                if (j % 2 != 1) {
                    Knowledge knowledge =new Knowledge();
                    String wt = page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[8]/div[" + j + "]/span/text()").toString();
                    if (StringUtils.isNotEmpty(wt)) {
                        knowledge.setQuestion(wt);
                    }
                    String daan = page.getHtml().xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div[8]/div[" + (j + 1) + "]/text()").toString();
                    if (StringUtils.isNotEmpty(daan)) {
                        knowledge.setAnswer(daan);
                    }
                    knowledgeList.add(knowledge);
                }
            }
        }
    }

    /**
     * 问题
     */
    private String question;
    /**
     * 答案
     */
    private String answer;

}
