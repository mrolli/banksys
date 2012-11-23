package ch.sws.ds.banksys.ebanking.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter Klasse für das URL-Rewriting.
 */
@WebFilter("/*")
public class URLFilter implements Filter {

    /**
     * Default constructor. 
     */
    public URLFilter() {
        // do nothing
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// do nothing
	}

	/**
	 * URL-Umschreibung gemäass PathInfo des Requests.
	 * 
	 * Requests auf statische Dateien (CSS, JavaScript, Bilder) werden ungefiltert
	 * an das Default Servlet weitergegeben.
	 * 
	 * Bei allen anderen Requests wir die PathInfo mit dem Präfix /pages versehen
	 * und an den FrontController weitergeleitet.
	 * 
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getRequestURI().substring(req.getContextPath().length());

		if (path.startsWith("/resources/")) {
		    // Just let container's default servlet do its job.
		    chain.doFilter(request, response);
		} else {
		    // Delegate to front controller.
		    request.getRequestDispatcher("/pages" + path).forward(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// do nothing
	}

}
