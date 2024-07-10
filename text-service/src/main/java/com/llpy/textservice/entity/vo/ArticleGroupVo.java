package com.llpy.textservice.entity.vo;

import lombok.Data;

/**
 * 文章组vo
 *
 * @author llpy
 * @date 2024/07/05
 */
@Data
public class ArticleGroupVo implements Comparable<ArticleGroupVo> {
    private Long id;

    private String articleType;

    private Integer number;

    @Override
    public int compareTo(ArticleGroupVo o) {
        return o.number.compareTo(this.number);
    }
}
