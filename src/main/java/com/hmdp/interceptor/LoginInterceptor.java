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
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession();
//        Object user = session.getAttribute(SystemConstants.USER);
//        if (Objects.isNull(user)){
//            log.info("无用户信息, 拦截");
//            throw new BusinessException(StatusEnum.NOT_USER);
//        }
//        UserDTO userDTO = BeanUtil.toBean(user, UserDTO.class);

        UserDTO user = UserHolder.getUser();
        if (Objects.isNull(user)){
            log.info("LoginInterceptor无用户信息, 拦截");
            response.setStatus(401);
            return false;
        }

        return true;
    }



}
