package com.shop.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: NoCacheFilter
 * @Description: 禁止浏览器缓存所有动态页面
 */
@WebFilter(filterName = "NoCacheFilter", urlPatterns = "*.jsp")
public class NoCacheFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        if (req == null || resp == null) {
            throw new ServletException("Request or Response is null");
        }

        // 把ServletRequest/Response强转成HttpServletRequest/Response
        if (!(req instanceof HttpServletRequest) || !(resp instanceof HttpServletResponse)) {
            throw new ServletException("Request and Response must be of type HttpServletRequest and HttpServletResponse");
        }

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 禁止浏览器缓存所有动态页面
        response.setDateHeader("Expires", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");

        // 继续请求链
        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化操作
    }

    public void destroy() {
        // 销毁操作
    }
}
