package com.yl.demo.controller;


import com.yl.demo.entity.Teacher;
import com.yl.demo.service.ITeacherService;
import com.yl.demo.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wfj
 * @since 2020-11-12
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private ITeacherService teacherService;

    /**
     * 添加老师
     * @param teacher
     * @return
     */
    @PostMapping("/addTeacher")
    public Result addTeacher(@RequestBody Teacher teacher) {
        Integer result = teacherService.addTeacher(teacher);
        if (result > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @GetMapping("/listFemaleTeacher")
    public Result listFemaleTeacher() {
        List<Teacher> teacherList = teacherService.getTeacherList();
        if (teacherList != null) {
            return Result.success(teacherList);
        } else {
            return Result.fail();
        }
    }
}

