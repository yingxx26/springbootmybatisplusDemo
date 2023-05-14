package com.yl.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yl.demo.entity.Student;
import com.yl.demo.entity.StudentTaskError;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wfj
 * @since 2020-11-12
 */
public interface LuaService {

    String testLua(String productCode, String quantity);

}
