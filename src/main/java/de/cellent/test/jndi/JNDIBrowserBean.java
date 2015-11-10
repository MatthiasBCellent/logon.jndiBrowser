package de.cellent.test.jndi;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.naming.Context;
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
@Remote(JNDIBrowser.class)
@Singleton
// if this one should be injected into a different jar
@EJB(name = "java:global/JNDIBrowser", beanInterface = JNDIBrowser.class)
public class JNDIBrowserBean {
	
    private static final Logger LOG = Logger.getLogger(JNDIBrowserBean.class);

    private InitialContext ctx;
 
    private HashMap<String, String> cache = new HashMap<String, String>();
	
    private String ctxRoot;


    @PostConstruct
    private void init() {
    	try {
			ctx = new InitialContext();
			ctxRoot = "java:global";
	    	this.readContext();
	    	ctxRoot = "java:jboss/exported";
	    	this.readContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    private void readContext() {
        try {
        	
            NamingEnumeration<NameClassPair> names = ctx.list(ctxRoot);

            // as long as we have something on this level
            while (names.hasMore()) {
                NameClassPair next = names.next();
                String name = next.getName();
                String clazzName = next.getClassName();

                // do we have a sub-context?
                if (clazzName.equals("javax.naming.Context")) {
                	
                    // this first-slash issue if no ear is present
                    if (ctxRoot.equals("")) {
                    	ctxRoot = name;
                    } else {
                    	ctxRoot = ctxRoot + "/" + name;
                    }
                    // read sub-context recursively
                    this.readContext();
                    
                } else {
                	LOG.debug("ContextName ==> " + ctxRoot);
                    String lookupString = (ctxRoot + name);
                    LOG.debug("Caching: " + clazzName + " -> " + lookupString);
                    cache.put(clazzName, lookupString);
                }
                if (!names.hasMore())
                	ctxRoot = "";
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // checking the cache and deliver ... or throw an exception
    public String getLookupString(Class clazz) throws NamingException {
        String ret = this.cache.get(clazz.getName());

        // maybe we had a deployment which isn't cached yet
        if (null == ret) {
            LOG.debug(clazz.getSimpleName() + " not found in cache going for reload of cache");
            this.readContext();
            ret = this.cache.get(clazz.getName());
            if (null == ret) {
                throw new NamingException(clazz.getSimpleName() + " not bound to JNDI");
            }
        }
        return ret;
    }
    
    
    @SuppressWarnings("unchecked")
	public <T> T getStub(Class<T> clazz) {
    	
    	T hit = null;
    	
    	try {
			hit = (T) ctx.lookup(this.cache.get(clazz.getName()));
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return hit;
    	
    }
    
    // all in a single shot ... maybe someone wants to cache it
    public HashMap<String, String> getAllBindings() {
    	return this.cache;
    }
}
