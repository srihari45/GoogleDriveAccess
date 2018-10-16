package gDrive.config;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class SessionListner implements HttpSessionListener {

	private Logger log = LogManager.getLogger(SessionListner.class);

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		log.debug("Session created");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log.debug("Session distroyed");
	}
}
