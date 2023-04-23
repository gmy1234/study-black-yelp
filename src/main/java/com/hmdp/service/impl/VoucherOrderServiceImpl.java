package com.hmdp.service.impl;

import ch.qos.logback.core.util.TimeUtil;
import cn.hutool.core.date.DateUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.Voucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IVoucherService;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import javafx.scene.control.Skin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private IVoucherService voucherService;

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Override
    @Transactional
    public Result seckillVoucher(Long voucherId) {
        // 获取 优惠券信息的 开始时间和过期时间
        Voucher voucher = voucherService.lambdaQuery()
                .eq(Voucher::getType, 1)
                .eq(Voucher::getId, voucherId)
                .one();
        if (Objects.isNull(voucher)){
            log.error("没有优惠券的信息");
            return Result.fail("没有优惠券的信息");
        }
        SeckillVoucher seckillVoucher = seckillVoucherService.getById(voucherId);
        if (Objects.isNull(seckillVoucher)){
            log.error("没有秒杀优惠券的信息");
            return Result.fail("没有秒杀优惠券的信息");
        }

        LocalDateTime beginTime = seckillVoucher.getBeginTime();
        LocalDateTime endTime = seckillVoucher.getEndTime();
        LocalDateTime now = LocalDateTime.now();
        if (! (now.isAfter(beginTime) && endTime.isAfter(now))){
            log.error("秒杀优惠券的日期未开始");
            return Result.fail("秒杀优惠券的日期未开始");
        }

        // 已经开始 减少库存
        int stock = seckillVoucher.getStock();
        if (stock < 1) {
            log.error("秒杀优惠券的库存不足");
            return Result.fail("秒杀优惠券的库存不足");
        }

        // 库存充足 减少 (乐观锁)
        seckillVoucher.setStock(seckillVoucher.getStock() - 1);
        boolean success = seckillVoucherService.lambdaUpdate()
                .gt(SeckillVoucher::getStock, 0)
                .eq(SeckillVoucher::getVoucherId, voucherId)
                .update(seckillVoucher);
        if (!success) {
            log.error("秒杀优惠券的库存减少失败");
            return Result.fail("秒杀优惠券的库存减少失败");
        }
        // 创建订单
        long orderId = redisIdWorker.nextId("order");
        VoucherOrder voucherOrder = VoucherOrder
                .builder()
                .id(orderId)
                .userId(123L)
                .voucherId(voucherId)
                .build();
        this.save(voucherOrder);

        return Result.ok(orderId);
    }
}
