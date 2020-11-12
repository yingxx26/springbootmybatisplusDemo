package com.yl.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.yl.demo.entity.Teacher;
import com.yl.demo.mapper.TeacherMapper;
import com.yl.demo.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    @Override
    public Integer addTeacher(Teacher teacher) {
        //调用ServiceImpl里的save方法，即可保存一个实体
        return this.save(teacher) == true ? 1 : 0;
    }

    @Override
    public List<Teacher> getTeacherList() {
        //新建一个条件构造器，这个条件构造器就相当于在where后面拼接条件了
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        //设置查询条件字段sex=1的，eq等于，lt小于，gt大于，还有很多可以到官网查看
        wrapper.eq("sex",1);
        List<Teacher> list = this.list(wrapper);
        return list;
    }
}
