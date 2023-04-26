package com.yl.demo.controller;


import com.yl.demo.entity.Student;
import com.yl.demo.service.IStudentService;
import com.yl.demo.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wfj
 * @since 2020-11-12
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    /**
     * 获取学生列表
     *
     * @return
     */
    @GetMapping("/listStudent")
    public Result listStudent() {
        List<Student> students = studentService.listStudent();
        if (students != null) {
            return Result.success(students);
        } else {
            return Result.fail();
        }
    }

    /**
     * 根据学生ID获取学生
     *
     * @param id
     * @return
     */
    @GetMapping("/getStudentById")
    public Result getStudentById(@RequestParam Integer id) {
        synchronized (this) {
            System.out.println("开始");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Student student = studentService.getStudentById(id);
            if (student != null) {
                return Result.success(student);
            } else {
                return Result.fail();
            }
        }
    }
}

