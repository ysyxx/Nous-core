// CourseServiceImpl.java
package com.service.impl;

// 导入MyBatis-Plus的Service实现类
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
// 导入Spring的Service注解，用于声明这是一个服务类
import com.entity.view.CourseView;
import com.entity.vo.CourseVO;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;
// 导入Spring的自动注入注解
import org.springframework.beans.factory.annotation.Autowired;

// 导入我们自己定义的DAO接口和Entity实体
import com.dao.CourseDao;
import com.entity.CourseEntity;
import com.service.CourseService;

import java.util.List;
import java.util.Map;


@Service("courseService") // 声明这是一个名为"courseService"的Spring Bean
public class CourseServiceImpl extends ServiceImpl<CourseDao, CourseEntity> implements CourseService {

    @Autowired // 自动注入CourseDao的实例
    private CourseDao courseDao;

    /**
     * 实现接口中定义的特殊方法，用于获取包含完整详情的课程
     * @param courseId 课程ID
     * @return 包含章节和课时列表的课程实体
     */
    @Override // 表明这是对接口方法的重写
    public CourseEntity getCourseWithDetails(Long courseId) {
        // 调用我们在CourseDao.xml中定义的自定义查询
        return courseDao.selectCourseWithDetails(courseId);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CourseEntity> page = this.selectPage(
                new Query<CourseEntity>(params).getPage(),
                new EntityWrapper<CourseEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<CourseEntity> wrapper) {
        Page<CourseView> page =new Query<CourseView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }


    @Override
    public List<CourseVO> selectListVO(Wrapper<CourseEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public CourseVO selectVO(Wrapper<CourseEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<CourseView> selectListView(Wrapper<CourseEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public CourseView selectView(Wrapper<CourseEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
