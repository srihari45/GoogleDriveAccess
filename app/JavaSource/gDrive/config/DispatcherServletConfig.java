package gDrive.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;

public class DispatcherServletConfig implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		servletContext.addListener(new SessionListner());
		System.out.println("Dispatcher Servelet");

	}

}
