package com.yl.demo.service;

import com.yl.demo.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wfj
 * @since 2020-11-12
 */
public interface ITeacherService extends IService<Teacher> {

    /**
     * 新增一名老师
     * @param teacher
     * @return
     */
    Integer addTeacher(Teacher teacher);

    /**
     * 获取所有女老师信息
     * @return
     */
    List<Teacher> getTeacherList();
}
