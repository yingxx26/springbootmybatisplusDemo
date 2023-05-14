package com.yl.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.demo.entity.Student;
import com.yl.demo.entity.StudentTaskError;
import com.yl.demo.mapper.StudentMapper;
import com.yl.demo.service.IDuoXcShiWuService;
import com.yl.demo.service.LuaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wfj
 * @since 2020-11-12
 */
@Service
public class LuaServiceImpl implements LuaService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String testLua(String productCode, String quantity) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        //设置返回值类型
        redisScript.setResultType(Long.class);
        //设置lua脚本文件路径
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/product.lua")));
        List<String> keys = new ArrayList<>();
        keys.add(productCode);
        Long resCode = (Long) redisTemplate.opsForValue().getOperations().execute(redisScript, keys, quantity);
        Assert.isTrue(resCode != null, "execute lua script error");
        String msg = "success to decrement stock";
        if (resCode == 1) {
            msg = "stock is not enough";
        } else if (resCode == 2) {
            msg = "product not exist";
        }
        return msg;
    }


}
