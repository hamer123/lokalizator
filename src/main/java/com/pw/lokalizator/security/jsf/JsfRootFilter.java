package com.pw.lokalizator.security.jsf;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.pw.lokalizator.model.session.LokalizatorSession;


/**
 * Servlet Filter implementation class JsfRootFilter
 */


@WebFilter(urlPatterns={
	"/app/*"
})
public class JsfRootFilter implements Filter {
	@Inject
	LokalizatorSession lokalizatorSession;
	private final String LOGIN_URI = "/login.xhtml";
	
    /**
     * Default constructor. 
     */
    public JsfRootFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		if(!lokalizatorSession.isLogged())
			res.sendRedirect(req.getContextPath() + LOGIN_URI);
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
