package com.service.impl;

import com.dao.ChapterDao;
import com.dto.ChapterWithLessonsDTO;
import com.entity.ChapterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;


import com.dao.LessonDao;
import com.entity.LessonEntity;
import com.service.LessonService;
import com.entity.vo.LessonVO;
import com.entity.view.LessonView;
import java.util.Comparator;

@Service("lessonService")
public class LessonServiceImpl extends ServiceImpl<LessonDao, LessonEntity> implements LessonService {

    @Autowired
    private ChapterDao chapterDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // MP 2.x 的分页查询
        Page<LessonEntity> page = new Page<>(
                Integer.parseInt(params.get("page").toString()),
                Integer.parseInt(params.get("limit").toString())
        );

        page = this.selectPage(
                page,
                new EntityWrapper<LessonEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ChapterWithLessonsDTO> getCourseStructureByCourseId(Long courseId) {

        List<ChapterEntity> chapters = chapterDao.selectList(
                new EntityWrapper<ChapterEntity>()
                        .eq("course_id", courseId)
                        .orderBy("chapter_order", true) // 按章节顺序升序
        );

        if (chapters == null || chapters.isEmpty()) {
            return new ArrayList<>(); // 如果没有章节，返回空列表
        }


        List<LessonEntity> lessons = this.selectList( // `this` 指的是 LessonService，调用其 selectList 方法
                new EntityWrapper<LessonEntity>()
                        .eq("course_id", courseId)
                        .orderBy("chapter_order", true)   // 先按章节ID排序
                        .orderBy("lesson_order", true) // 再按课时顺序排序
        );

        Map<Long, List<LessonEntity>> lessonsByChapterOrder = lessons.stream()
                .collect(Collectors.groupingBy(LessonEntity::getChapterOrder));

        // 3. 组装数据为 ChapterWithLessonsDTO 列表
        List<ChapterWithLessonsDTO> chapterWithLessonsList = chapters.stream().map(chapter -> {
            ChapterWithLessonsDTO dto = new ChapterWithLessonsDTO();
            // 复制 ChapterEntity 的属性到 DTO
            dto.setId(chapter.getId());
            dto.setCourseId(chapter.getCourseId());
            dto.setTitle(chapter.getTitle());
            dto.setChapterOrder(chapter.getChapterOrder());

            // 设置当前章节下的课时列表
            List<LessonEntity> chapterLessons = lessonsByChapterOrder.getOrDefault(chapter.getChapterOrder(), new ArrayList<>());

            chapterLessons.sort(Comparator.comparing(LessonEntity::getLessonOrder));
            dto.setLessons(chapterLessons);

            return dto;
        }).collect(Collectors.toList());

        return chapterWithLessonsList;
    }

    @Override
    public List<LessonVO> selectListVO(Wrapper<LessonEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public LessonVO selectVO(Wrapper<LessonEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<LessonView> selectListView(Wrapper<LessonEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<LessonEntity> wrapper) {
        Page<LessonView> page = new Query<LessonView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public LessonView selectView(Wrapper<LessonEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }

    // 如果你在 LessonService 中添加了自定义方法，例如：
    // @Override
    // public LessonEntity getLessonVideoUrl(Long chapterId, Integer lessonOrder) {
    //     EntityWrapper<LessonEntity> ew = new EntityWrapper<>();
    //     ew.eq("chapter_id", chapterId);
    //     ew.eq("lesson_order", lessonOrder);
    //     return this.selectOne(ew); // 或者调用 baseMapper.selectByChapterIdAndOrder(chapterId, lessonOrder);
    // }
}
