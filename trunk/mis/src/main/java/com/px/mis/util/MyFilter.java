package com.px.mis.util;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(MyFilter.class);

	private boolean isCross = false;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try
		{
			// TODO Auto-generated method stub
			String isCrossStr = filterConfig.getInitParameter("IsCross");
	        isCross = isCrossStr.equals("true") ? true : false;
			logger.info("MyFilter初始化："+isCross);
		}
		catch(Exception ex)
		{
			logger.info("MyFilter初始化异常：" + ex.getMessage());
			throw new ServletException("MyFilter初始化异常："+ex.getMessage());
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		if (isCross) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            System.err.println("拦截请求: " + httpServletRequest.getServletPath());
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            httpServletResponse.setHeader("Access-Control-Max-Age", "0");
            httpServletResponse.setHeader("Access-Control-Allow-Headers",
                    "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,accNum,loginTime,token");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.setHeader("XDomainRequestAllowed", "1");
        }
        chain.doFilter(request, response);
        System.err.println("chain.doFilter(request, response)");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
        isCross = false;
	}

}
