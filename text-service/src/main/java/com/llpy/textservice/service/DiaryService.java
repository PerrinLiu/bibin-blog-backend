package com.llpy.textservice.service;


import com.llpy.model.Result;
import com.llpy.textservice.entity.vo.DiaryVo;

import java.util.List;

/**
 * 日记服务
 *
 * @author llpy
 * @date 2024/06/26
 */
public interface DiaryService {
    Result<?> addDiary(DiaryVo diaryVo, Long id);

    /**
     * 获取日记,所有状态都会返回，根据用户id，获得用户的所有日记，如果id为空，则是全部
     *
     * @param userId   用户id
     * @param pageSize 页面大小
     * @param pageNum  书籍页码
     * @return {@code Result<List<DiaryVo>>}
     */
    Result<?> getDiary(Long userId, Integer pageSize, Integer pageNum, Integer status, String searchText);

    /**
     * 获取日记库，返回以通过
     *
     * @param userId   用户id
     * @param pageSize 页面大小
     * @param pageNum  书籍页码
     * @return {@code Result<?>}
     */
    Result<?> getDiaryBase(Long userId, Integer pageSize, Integer pageNum);

    /**
     * 获取一条文本
     *
     * @param diaryId 日记id
     * @return {@code Result<DiaryVo>}
     */
    Result<DiaryVo> getOneText(Long diaryId);

    /**
     * 删除日记一
     *
     * @param diaryId 日记id
     * @return {@code Result}
     */
    Result deleteDiaryOne(Long diaryId);

    Result<?> rejectDiary(Long diaryId, String rejectReason,Long userId);

    Result<?> passDiary(Long diaryId,Long userId);
}
