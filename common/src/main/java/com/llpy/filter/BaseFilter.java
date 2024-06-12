package com.llpy.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.llpy.enums.GatewayKey;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求拦截，避免服务绕过接口被直接访问,排在网关之后
 */
@Component
@WebFilter(filterName = "BaseFilter", urlPatterns = {"/**"})
@Order(999)
public class BaseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入过滤器========");

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 返回JSON数据，设置响应内容类型为application/json
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //获取请求头
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String gateway = request.getHeader(GatewayKey.GATEWAY_KEY.getKey());

        //判断是否携带了密钥
        if (gateway == null || !gateway.equals(GatewayKey.GATEWAY_KEY.getKeyInfo())) {
            //如果不携带密钥，返回403错误
            // 创建一个包含 code 和 message 的 Java 对象
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("code", 403);
            jsonResponse.put("message", "请通过网关访问资源");
            // 转换为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(jsonResponse);

            response.getWriter().write(jsonData);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("destroy filter");
    }

}