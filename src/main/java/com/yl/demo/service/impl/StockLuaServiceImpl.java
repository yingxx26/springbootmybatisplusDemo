package com.yl.demo.service.impl;

import com.yl.demo.service.LuaService;
import com.yl.demo.service.StockLuaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wfj
 * @since 2020-11-12
 */
@Service
public class StockLuaServiceImpl implements StockLuaService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String testLua(String productCode, String quantity) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        //设置返回值类型
        redisScript.setResultType(Long.class);
        //设置lua脚本文件路径
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/stock.lua")));
        List<String> keys = new ArrayList<>();
        keys.add(productCode);
        Long resCode = (Long) redisTemplate.execute(redisScript, keys, quantity);
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

//读取LUA脚本private static DefaultRedisScript<Long> SECKILL_SCRIPT;static {SECKILL_SCRIPT = new DefaultRedisScript<>();SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));SECKILL_SCRIPT.setResultType(Long.class);}//阻塞队列private BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024*1024);//线程池private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();//代理对象private IVoucherOrderService proxy;//程序启动就运行该异步订单的线程@PostConstructprivate void init(){SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());}//异步线程订单存库private class VoucherOrderHandler implements Runnable{@Overridepublic void run() {while (true){try {//1. 获取队列中的订单信息VoucherOrder voucherOrder = orderTasks.take();//2. 创建订单handleVoucherOrder(voucherOrder);} catch (Exception e) {log.error("处理订单异常", e);}}}}private void handleVoucherOrder(VoucherOrder voucherOrder) {//1. 获取用户Long userId = voucherOrder.getUserId();//2. 创建锁对象RLock lock = redissonClient.getLock("order:" + userId);//3. 获取锁 tryLock()默认 -1 30 Secondboolean isLock = lock.tryLock();//4. 判断是否获取锁成功if(!isLock){//获取所失败，返回错误或重试log.error("不允许重复下单");return ;}try {proxy.createVoucherOrder(voucherOrder);}finally {lock.unlock();}}@Overridepublic Result seckillVoucher(Long voucherId) {//获取用户Long userId = UserHolder.getUser().getId();//1.执行LUA脚本Long result = stringRedisTemplate.execute(SECKILL_SCRIPT,Collections.emptyList(),voucherId.toString(), userId.toString());int r = result.intValue();//2. 判断结果是否为0if(r != 0){//2.1 不为0，代表没有购买资格return Result.fail(r==1?"库存不足":"不能重复下单");}//2.2 为0，有购买资格，把下单信息保存至阻塞队列VoucherOrder voucherOrder = new VoucherOrder();//2.3 订单ID--全局唯一ID生成器Long orderId = redisIDWorker.nextId("order");voucherOrder.setId(orderId);//2.4 用户idvoucherOrder.setUserId(userId);//2.5 代金券idvoucherOrder.setVoucherId(voucherId);//2.6 放入阻塞队列orderTasks.add(voucherOrder);//3. 获取代理对象proxy = (IVoucherOrderService) AopContext.currentProxy();//4. 返回订单idreturn Result.ok(orderId);}@Transactional@Overridepublic void createVoucherOrder(VoucherOrder voucherOrder) {Long voucherId = voucherOrder.getVoucherId();//扣减库存boolean updateFlag = seckillVoucherService.lambdaUpdate().setSql("stock = stock -1").eq(SeckillVoucher::getVoucherId, voucherId).gt(SeckillVoucher::getStock, 0).update();if (!updateFlag) {//扣减失败log.error("库存不足");return;}//7. 创建订单save(voucherOrder);}
