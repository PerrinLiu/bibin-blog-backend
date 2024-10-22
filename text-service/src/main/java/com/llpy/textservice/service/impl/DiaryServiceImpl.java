package com.llpy.textservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llpy.config.BizException;
import com.llpy.enums.EmailEnum;
import com.llpy.enums.GatewayKey;
import com.llpy.enums.ResponseError;
import com.llpy.model.Result;
import com.llpy.textservice.entity.Diary;
import com.llpy.textservice.entity.DiaryText;
import com.llpy.textservice.entity.dto.AssessDiaryDto;
import com.llpy.textservice.entity.vo.DiaryVo;
import com.llpy.textservice.feign.UserService;
import com.llpy.textservice.mapper.DiaryMapper;
import com.llpy.textservice.mapper.DiaryTextMapper;
import com.llpy.textservice.mapper.PhotoWallMapper;
import com.llpy.textservice.service.DiaryService;
import com.llpy.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日记服务impl
 *
 * @author llpy
 * @date 2024/07/04
 */
@Service
public class DiaryServiceImpl implements DiaryService {
    private final DiaryMapper diaryMapper;
    private final DiaryTextMapper diaryTextMapper;

    @Autowired
    private UserService userService;

    private final PhotoWallMapper photoWallMapper;

    public DiaryServiceImpl(DiaryMapper diaryMapper, DiaryTextMapper diaryTextMapper, PhotoWallMapper photoWallMapper) {
        this.diaryMapper = diaryMapper;
        this.diaryTextMapper = diaryTextMapper;
        this.photoWallMapper = photoWallMapper;
    }

    @Override
    @Transactional  //开启事务，确保同时成功，或同时失败，保持数据一致性
    public Result<?> addDiary(DiaryVo diaryVo, Long id) {
        if(id == null){
            return Result.error("会话过期");
        }



        //如果是隐私的日记，需要将图片也一同标记为隐私
        if(diaryVo.getIsOpen()==1){
            List<String> strings = DataUtils.extractImgSrc(diaryVo.getDiaryText());
            if (!strings.isEmpty()){
                photoWallMapper.updateImgToPrivate(strings,id);
            }

        }
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

        diary.setUserId(id).setDiaryId(null).setIsOpen(diaryVo.getIsOpen()).setDiaryTextId(textId)
                .setDiaryTitle(diaryVo.getDiaryTitle()).setCreateTime(LocalDateTime.now()).setDiaryStatus(1);

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
     * 根据用户id，获得用户的所有日记，如果id为空，则是全部
     *
     * @return list
     */
    @Override
    public Result<?> getDiary(Long userId, Integer pageSize, Integer pageNum,Integer status,String searchText) {
        Page<DiaryVo> diaryVoPage = new Page<>(pageNum, pageSize);

        diaryMapper.getList(diaryVoPage,userId,status,searchText);

        return Result.success(diaryVoPage);
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

        List<DiaryVo> records = diaryVoPage.getRecords();
        //根据日期对日记进行排序
        records.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));

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

    @Override
    @Transactional
    public Result<?> rejectDiary(AssessDiaryDto assessDiaryDto, Long userId) {
        Diary diary = diaryMapper.selectById(assessDiaryDto.getDiaryId());
        if(diary==null){
            throw new BizException(ResponseError.COMMON_ERROR);
        }
        diary.setDiaryStatus(3).setPassUser(userId);
        diaryMapper.updateById(diary);
        //发送邮件通知
        Long diaryUserId = diary.getUserId();
        String userEmail = userService.getUserEmail(diaryUserId);
        //添加分割符号
        String message = diary.getDiaryTitle()+ GatewayKey.GATEWAY_KEY.getKeyInfo() +assessDiaryDto.getRejectReason();
        return userService.sendEmail(userEmail, EmailEnum.DIARY_REJECT.getKey(), message);
    }

    @Override
    @Transactional
    public Result<?> passDiary(Long diaryId,Long userId) {
        Diary diary = diaryMapper.selectById(diaryId);
        if(diary==null){
            throw new BizException(ResponseError.COMMON_ERROR);
        }
        diary.setDiaryStatus(2).setPassUser(userId);
        diaryMapper.updateById(diary);
        // 发送邮箱通知
        Long diaryUserId = diary.getUserId();
        String userEmail = userService.getUserEmail(diaryUserId);
        return userService.sendEmail(userEmail, EmailEnum.DIARY_PASS.getKey(), diary.getDiaryTitle());
    }
}
