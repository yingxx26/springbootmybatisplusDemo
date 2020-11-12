package com.yl.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yl.demo.entity.Student;
import com.yl.demo.mapper.StudentMapper;
import com.yl.demo.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wfj
 * @since 2020-11-12
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Resource
    private StudentMapper studentMapper;

    /**
     * 这里调的是BaseMapper里的方法，List<T> selectList(@Param("ew") Wrapper<T> queryWrapper);
     * @return
     */
    @Override
    public List<Student> listStudent() {
        return this.studentMapper.selectList(new QueryWrapper<>());
    }

    /**
     * 这里调的是ServiceImpl里面的方法,
     * @param id
     * @return
     */
    @Override
    public Student getStudentById(Integer id) {
        return this.getById(id);
    }
}
