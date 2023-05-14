package com.yl.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yl.demo.entity.Student;
import com.yl.demo.entity.StudentTaskError;
import com.yl.demo.mapper.StudentMapper;
import com.yl.demo.service.IDuoXcShiWuService;
import com.yl.demo.service.LuaService;
import com.yl.demo.service.StockLuaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
class DemoApplicationTests {

    //多线程事务  核心：TransactionStatus

    @Autowired
    private IDuoXcShiWuService duoXcShiWuService;

    @Autowired
    private LuaService luaService;

    @Autowired
    private StockLuaService stockLuaService;


    @Resource
    private StudentMapper studentMapper;

    @Test
    void contextLoads() {


    }

    @Test
    void updateStudentWithThreadsAndTrans() {
        //查询总数据
        List<Student> allStudents = studentMapper.selectList(new QueryWrapper<>());
        // 线程数量
        final Integer threadCount = 4;

        //每个线程处理的数据量
        final Integer dataPartionLength = (allStudents.size() + threadCount - 1) / threadCount;

        // 创建多线程处理任务
        ExecutorService studentThreadPool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch threadLatchs = new CountDownLatch(threadCount); // 用于计算子线程提交数量
        CountDownLatch mainLatch = new CountDownLatch(1); // 用于判断主线程是否提交
        StudentTaskError taskStatus = new StudentTaskError(); // 用于判断子线程任务是否有错误

        for (int i = 0; i < threadCount; i++) {
            // 每个线程处理的数据
            List<Student> threadDatas = allStudents.stream()
                    .skip(i * dataPartionLength).limit(dataPartionLength)
                    .collect(Collectors.toList());
            studentThreadPool.execute(() -> {
                duoXcShiWuService.updateStudentsThread(threadDatas, threadLatchs, mainLatch, taskStatus);
            });
        }
        try {
            // 倒计时锁设置超时时间 30s
            System.out.println("threadLatchs.await()");
            boolean await = threadLatchs.await(10, TimeUnit.SECONDS);
            System.out.println("threadLatchs.await()结束");
            if (!await) { // 等待超时，事务回滚
                taskStatus.setIsError();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            taskStatus.setIsError();
        }
        /*System.out.println("mainLatch.countDown()");
        mainLatch.countDown(); // 切换到子线程执行
        System.out.println("mainLatch.countDown()结束");*/
        studentThreadPool.shutdown(); //关闭线程池

        System.out.println("主线程完成");
    }

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    void testOne1() {
        redisTemplate.opsForValue().set("name", "卷心菜");
        String name = (String) redisTemplate.opsForValue().get("name");
        System.out.println(name); //卷心菜
    }

    @Test
    void testlua() {
        luaService.testLua("p1", "1");
    }

    @Test
    void testlua1() {
        stockLuaService.testLua("1", "yxx");
    }
}
