package com.hmdp.service.impl;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.entity.User;
import com.hmdp.enums.StatusEnum;
import com.hmdp.exception.BusinessException;
import com.hmdp.ibo.SendCodeIBO;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.constants.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Random;

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

    @Override
    public long sendCode(SendCodeIBO phoneIBO) {
        String phone = phoneIBO.getPhone();
        boolean isPhone = PhoneUtil.isPhone(phone);
        HttpSession httpSession = phoneIBO.getHttpSession();
        if (!isPhone) {
            throw new BusinessException(StatusEnum.NOT_PHONE);
        }
        long code = this.generateNumber(4);
        httpSession.setAttribute(phone, code);

        return code;
    }

    private long generateNumber(int length) {
        Random random = new Random();
        int min = (int) Math.pow(10, length - 1); // 最小值为 1000
        int max = (int) Math.pow(10, length) - 1; // 最大值为 9999
        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public void login(LoginFormDTO loginForm, HttpSession session) {
        String phone = loginForm.getPhone();
        boolean isPhone = PhoneUtil.isPhone(phone);
        if (!isPhone) {
            throw new BusinessException(StatusEnum.NOT_PHONE);
        }
        long code = loginForm.getCode();
        Object cacheCode = session.getAttribute(phone);
        // 校验手机号
        if (Objects.isNull(cacheCode) || Long.parseLong(cacheCode.toString()) != code){
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
        session.setAttribute("user", user);
        return;
    }


}
