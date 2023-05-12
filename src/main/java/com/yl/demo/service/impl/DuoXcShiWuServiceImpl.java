package com.yl.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.demo.entity.Student;
import com.yl.demo.entity.StudentTaskError;
import com.yl.demo.mapper.StudentMapper;
import com.yl.demo.service.IDuoXcShiWuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
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
public class DuoXcShiWuServiceImpl extends ServiceImpl<StudentMapper, Student> implements IDuoXcShiWuService {

    @Resource
    private StudentMapper studentMapper;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    @Override
    public void updateStudentsThread(List<Student> students, CountDownLatch threadLatch, CountDownLatch mainLatch, StudentTaskError taskStatus) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        System.out.println("子线程：" + Thread.currentThread().getName());
        AtomicInteger myi = new AtomicInteger();
        try {
            students.forEach(s -> {
                // 更新教师信息
                // String teacher = s.getTeacher();
                String sname = "TNO_" + new Random().nextInt(100);
                s.setSname(sname);

                UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", s.getId());
                // 1.user 中封装修改的属性值， updateWrapper 中封装修改的条件参数值
                studentMapper.update(s, updateWrapper);
                myi.set(s.getId());
                if (s.getId() == 6) {
                    //int i = 1 / 0;
                }
            });
        } catch (Throwable e) {
            taskStatus.setIsError();
            System.out.println(myi.get());
        } finally {
            System.out.println("threadLatch.countDown()");
            threadLatch.countDown(); // 切换到主线程执行
            System.out.println("threadLatch.countDown()结束");
        }
        /*try {
            System.out.println("mainLatch.await()");
            mainLatch.await();  //等待主线程执行
            System.out.println("mainLatch.await()结束");
        } catch (Throwable e) {
            taskStatus.setIsError();
        }*/
        // 判断是否有错误，如有错误 就回滚事务
        if (taskStatus.getIsError()) {
            dataSourceTransactionManager.rollback(transactionStatus);
        } else {
            dataSourceTransactionManager.commit(transactionStatus);
        }
    }
}
