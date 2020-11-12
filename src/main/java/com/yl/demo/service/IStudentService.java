package com.yl.demo.service;

import com.yl.demo.entity.Student;
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
public interface IStudentService extends IService<Student> {
    /**
     * 获取学生列表
     * @return
     */
    List<Student> listStudent();

    /**
     * 根据学生ID查找学生
     * @param id
     * @return
     */
    Student getStudentById(Integer id);
}
