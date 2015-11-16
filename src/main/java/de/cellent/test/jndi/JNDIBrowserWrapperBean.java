package de.cellent.test.jndi;

import java.rmi.NotBoundException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * Browsing the JNDI tree on startup of the server.
 *
 * TODO think of a "reload" as on every deployment the server has to be
 * restarted. Maybe a caching framework might help (some kind of onEviction
 * listener) or the app server should fire an event on deployment. for the
 * moment this is enough ...
 *
 * @author mbohnen
 * @param <T>
 *
 */
@Remote(JNDIBrowserWrapper.class)
@Singleton
// if this one should be injected into a different jar
@EJB(name = "java:global/JNDIBrowser", beanInterface = JNDIBrowserWrapper.class)
public class JNDIBrowserWrapperBean implements JNDIBrowserWrapper {
	
	@Inject
	private JNDIBrowser browser;
	
    private static final Logger LOG = Logger.getLogger(JNDIBrowserWrapperBean.class);

	@Override
	public String getLookupString(Class clazz) {
		String ret = "";
		try {
			ret = browser.getLookupString(clazz);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return ret;
	}

	@Override
	public HashMap<String, String> getAllBindings() {
		return browser.getAllBindings();
	}

	@Override
	public <T> T getStub(Class<T> clazz) {
		return browser.getStub(clazz);
	}
}
