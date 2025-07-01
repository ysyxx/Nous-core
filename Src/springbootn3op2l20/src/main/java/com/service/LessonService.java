// 文件路径: src/main/java/com/service/LessonService.java

package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.dto.ChapterWithLessonsDTO;
import com.utils.PageUtils;
import com.entity.LessonEntity;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.vo.LessonVO;
import com.entity.view.LessonView;

/**
 * 课时服务接口
 *
 * @author
 * @email
 * @date 2025-06-25
 */
public interface LessonService extends IService<LessonEntity> {
    PageUtils queryPage(Map<String, Object> params);

    List<LessonVO> selectListVO(Wrapper<LessonEntity> wrapper);

    LessonVO selectVO(@Param("ew") Wrapper<LessonEntity> wrapper);

    List<LessonView> selectListView(Wrapper<LessonEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params,Wrapper<LessonEntity> wrapper);

    LessonView selectView(@Param("ew") Wrapper<LessonEntity> wrapper);

    List<ChapterWithLessonsDTO> getCourseStructureByCourseId(Long courseId);
}
