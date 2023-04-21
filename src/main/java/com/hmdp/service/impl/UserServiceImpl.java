package com.hmdp.service.impl;

import ch.qos.logback.core.util.TimeUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.constants.RedisConstants;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.enums.StatusEnum;
import com.hmdp.exception.BusinessException;
import com.hmdp.ibo.SendCodeIBO;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.constants.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.KSQLJoinWindow;
import org.apache.catalina.UserDatabase;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public long sendCode(SendCodeIBO phoneIBO) {
        String phone = phoneIBO.getPhone();
        boolean isPhone = PhoneUtil.isPhone(phone);
        // HttpSession httpSession = phoneIBO.getHttpSession();
        if (!isPhone) {
            throw new BusinessException(StatusEnum.NOT_PHONE);
        }
        final String code = RandomUtil.randomNumbers(4);
        // redis 设置key
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + phone,
                code, RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);

        return Long.parseLong(code);
    }

    private long generateNumber(int length) {
        Random random = new Random();
        int min = (int) Math.pow(10, length - 1); // 最小值为 1000
        int max = (int) Math.pow(10, length) - 1; // 最大值为 9999
        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        String phone = loginForm.getPhone();
        boolean isPhone = PhoneUtil.isPhone(phone);
        if (!isPhone) {
            throw new BusinessException(StatusEnum.NOT_PHONE);
        }
        long code = loginForm.getCode();
        // Object cacheCode = session.getAttribute(phone);
        final String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
        // 校验手机号
        if (Objects.isNull(cacheCode) || Long.parseLong(cacheCode) != code){
            log.error("手机号{}的验证码错误", phone);
            throw new BusinessException(StatusEnum.NOT_PHONE_CODE);
        }
        // 查数据有没有该用户
        User user = this.lambdaQuery().eq(User::getPhone, phone).one();
        if (Objects.isNull(user)){
            user = User.builder()
                    .phone(phone)
                    .nickName(SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(6))
                    .build();
            this.save(user);
        }
        // session.setAttribute("user", user);
        // 生成token
        final String token = UUID.randomUUID().toString();
        // 用户转hash，存储redis
        final UserDTO userDTO = BeanUtil.toBean(user, UserDTO.class);
        // 存用户
        // user 的 ID 为long，redis序列化不了为string，需要将userDTO中的字段值都改为string
        Map<String, Object> stringUserDTOMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions
                        .create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(RedisConstants.LOGIN_USER_KEY + token, stringUserDTOMap);
        // 设置过期时间
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token, 30, TimeUnit.MINUTES);

        return Result.ok(token);
    }


}
