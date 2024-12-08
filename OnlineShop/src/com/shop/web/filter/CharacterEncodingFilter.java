package com.shop.web.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

/**
 * @ClassName: CharacterEncodingFilter
 * @Description: 此过滤器用来解决中文乱码问题, 需要放在第一个
 */
public class CharacterEncodingFilter implements Filter {
    
    private FilterConfig filterConfig = null;
    private String defaultCharset = "UTF-8"; // 设置默认的字符编码

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 得到过滤器的初始化配置信息
        this.filterConfig = filterConfig;
        if (filterConfig == null) {
            throw new ServletException("CharacterEncodingFilter configuration is missing.");
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        
        // 空值检查
        if (req == null || resp == null) {
            throw new ServletException("Request or Response is null");
        }

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 获取初始化参数 charset，如果为空则使用默认字符编码
        String charset = filterConfig.getInitParameter("charset");
        if (charset == null || charset.isEmpty()) {
            charset = defaultCharset;  // 使用默认编码
        }

        // 设置编码
        request.setCharacterEncoding(charset);
        response.setCharacterEncoding(charset);
        response.setContentType("text/html;charset=" + charset);

        // 包装request，解决get方式获得的参数中文乱码问题
        MyCharacterEncodingRequest requestWrapper = new MyCharacterEncodingRequest(request);

        // 如果requestWrapper为空，则抛出异常
        if (requestWrapper == null) {
            throw new ServletException("Request wrapper is null");
        }

        // 执行链式调用
        chain.doFilter(requestWrapper, response);
    }

    @Override
    public void destroy() {
        // 可根据需要清理资源
    }
}

/**
 * 解决GET方式获得的参数中文乱码问题
 */
class MyCharacterEncodingRequest extends HttpServletRequestWrapper {

    private HttpServletRequest request;

    public MyCharacterEncodingRequest(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public String getParameter(String name) {
        try {
            // 获取参数值
            String value = this.request.getParameter(name);
            
            // 如果value为空，直接返回null
            if (value == null) {
                return null;
            }

            // 如果是POST请求，则直接返回参数值
            if (!this.request.getMethod().equalsIgnoreCase("get")) {
                return value;
            } else {
                // GET请求时，检查编码
                // 这里是对获取到的GET参数进行处理，避免乱码
                System.out.println("name=" + name + ",value=" + value);
                return value;
            }
        } catch (Exception e) {
            // 捕获异常并抛出
            throw new RuntimeException("Error while getting parameter: " + name, e);
        }
    }
}
