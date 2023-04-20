package com.hmdp.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.constants.RedisConstants;
import com.hmdp.constants.SystemConstants;
import com.hmdp.dto.UserDTO;
import com.hmdp.enums.StatusEnum;
import com.hmdp.exception.BusinessException;
import com.hmdp.utils.UserHolder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author gmydl
 * @title: LoginInterceptor
 * @projectName yelp
 * @description: TODO
 * @date 2023/4/20 15:57
 */
@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession();
//        Object user = session.getAttribute(SystemConstants.USER);
//        if (Objects.isNull(user)){
//            log.info("无用户信息, 拦截");
//            throw new BusinessException(StatusEnum.NOT_USER);
//        }
//        UserDTO userDTO = BeanUtil.toBean(user, UserDTO.class);

        // 得token
        final String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)){
            log.info("无用户token, 拦截");
            response.setStatus(401);
            return false;
        }
        final Map<Object, Object> userMap = stringRedisTemplate.opsForHash()
                .entries(RedisConstants.LOGIN_USER_KEY + token);
        if (userMap.isEmpty()) {
            log.info("无用户信息, 拦截");
            response.setStatus(401);
            return false;
        }
        final UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), true);
        UserHolder.saveUser(userDTO);

        // 刷新TOKEN
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token, 30, TimeUnit.MINUTES);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }



}
