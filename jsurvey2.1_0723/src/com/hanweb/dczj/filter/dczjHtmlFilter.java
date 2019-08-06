package com.hanweb.dczj.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.hanweb.common.BaseInfo;
import com.hanweb.complat.constant.Settings;

public class dczjHtmlFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (Settings.getSettings().getClosesystem()==1) {
			try {
				((HttpServletResponse) response).sendRedirect(BaseInfo.getContextPath() + "/systemclose.html");
			} catch (Exception e) {
				
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

}
