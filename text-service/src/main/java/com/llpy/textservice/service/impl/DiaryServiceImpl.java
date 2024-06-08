package com.llpy.textservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.model.Result;
import com.llpy.textservice.entity.Diary;
import com.llpy.textservice.entity.DiaryText;
import com.llpy.textservice.entity.vo.DiaryVo;
import com.llpy.textservice.mapper.DiaryMapper;
import com.llpy.textservice.mapper.DiaryTextMapper;
import com.llpy.textservice.service.DiaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiaryServiceImpl implements DiaryService {
    private final DiaryMapper diaryMapper;
    private final DiaryTextMapper diaryTextMapper;

    public DiaryServiceImpl(DiaryMapper diaryMapper, DiaryTextMapper diaryTextMapper) {
        this.diaryMapper = diaryMapper;
        this.diaryTextMapper = diaryTextMapper;
    }

    @Override
    @Transactional  //开启事务，确保同时成功，或同时失败，保持数据一致性
    public Result addDiary(DiaryVo diaryVo, Long id) {
        if (id == null) return Result.error("会话过期");

        //新建日记信息对象
        DiaryText diaryText = new DiaryText();
        //设置文章内容
        diaryText.setDiaryTextId(null).setDiaryId(null)
                .setDiaryText(diaryVo.getDiaryText())
                .setCreateTime(LocalDateTime.now());
        //添加到数据库
        diaryTextMapper.insert(diaryText);
        //插入后获得生成的日记信息id
        Long textId = diaryText.getDiaryTextId();

        //添加日记信息id到日记基本信息表，然后插入数据库
        Diary diary = new Diary();

        diary.setUserId(id)
                .setDiaryId(null)
                .setIsOpen(diaryVo.getIsOpen())
                .setDiaryTextId(textId)
                .setDiaryTitle(diaryVo.getDiaryTitle())
                .setCreateTime(LocalDateTime.now());

        //往数据库添加日记
        diaryMapper.insert(diary);

        //获得日记id
        Long diaryId = diary.getDiaryId();

        //更新日记信息的日记id
        diaryText.setDiaryId(diaryId);

        diaryTextMapper.updateById(diaryText);

        return Result.success();
    }

    /**
     * 获取所有日记
     *
     * @return
     */
    @Override
    public Result getDiary() {
        List<DiaryVo> list = diaryMapper.getList();

        return Result.success(list);
    }

    /**
     * 把日记
     *
     * @param userId 用户id
     * @return {@link Result}<{@link List}<{@link DiaryVo}>>
     */
    @Override
    public Result getDiary(Long userId) {
        //查询当前用户的所有文章
        List<DiaryVo> list = diaryMapper.getListById(userId);
        return Result.success(list);
    }

    /**
     * 得到日记基础信息
     *
     * @return {@link Result}<{@link List}<{@link Diary}>>
     */
    @Override
    public Result<?> getDiaryBase(Long userId, Integer pageSize, Integer pageNum) {
        Page<DiaryVo> diaryVoPage = new Page<>(pageNum, pageSize);
        //得到基本信息
        diaryMapper.getListTitle(diaryVoPage, userId);

        return Result.success(diaryVoPage);
    }

    /**
     * 得到单个日记内容
     *
     * @param diaryId 日记id
     * @return {@link Result}<{@link DiaryVo}>
     */
    public Result<DiaryVo> getOneText(Long diaryId) {

        DiaryVo oneText = diaryMapper.getOneText(diaryId);

        return Result.success(oneText);
    }

    /**
     * 删除日记
     *
     * @param diaryId 日记id
     * @return {@link Result}
     */
    @Override
    @Transactional
    public Result deleteDiaryOne(Long diaryId) {
        //删除日记基础信息
        diaryMapper.deleteById(diaryId);
        //创建一个根据日记id删除日记内容的lambda表达式，删除日记内容
        LambdaQueryWrapper<DiaryText> diaryQuery = new LambdaQueryWrapper<>();

        diaryQuery.eq(DiaryText::getDiaryId, diaryId);

        diaryTextMapper.delete(diaryQuery);

        return Result.success();
    }
}
