package com.hmdp.ibo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * @author gmydl
 * @title: SendCodeIBO
 * @projectName yelp
 * @description: TODO
 * @date 2023/4/20 15:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendCodeIBO implements Serializable {

    private String phone;

    private HttpSession httpSession;
}
