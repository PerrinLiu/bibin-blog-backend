package com.llpy.userservice.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 计数数据vo
 *
 * @author llpy
 * @date 2024/07/03
 */
@Data
public class CountDataVo {
    /**
     * 文章计数
     */
    private int articleCount;
    /**
     * 组计数
     */
    private int groupCount;
    /**
     * 访问次数
     */
    private int visitCount;

    private List<CommitData> commitDataList;

    private String[] monthBar;

    /**
     * 提交数据
     *
     * @author llpy
     * @date 2024/07/03
     */
    @Data
    @Accessors(chain = true)
    public static class CommitData {
        private String date;
        private String year;
        private String month;
        private int number;
        private int level;
        private boolean today;
    }
}
