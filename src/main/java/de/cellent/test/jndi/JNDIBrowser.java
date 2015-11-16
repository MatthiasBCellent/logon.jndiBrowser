package de.cellent.test.jndi;

import java.util.HashMap;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

@Singleton
@ManagedBean
@Startup
public class JNDIBrowser {

	// just a logger
    private static final Logger LOG = Logger.getLogger(JNDIBrowserWrapperBean.class);
    
    // where the strings will be cached
    private HashMap<String, String> cache = new HashMap<String, String>();

    // well ... the initial context
    private InitialContext ctx;
 	
    // from where to start
    private String ctxRoot;

    // this is 
	private String initialRoot;

    @PostConstruct
    private void init() {
    	try {
			ctx = new InitialContext();
			
			//initially we inspect the java:global context
			this.setRoot("java:global/");
	    	this.readContext();
	    	
	    	// then we will go for more global things
	    	this.setRoot("java:jboss/exported/");
	    	this.readContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void setRoot(String root) {
    	
    	// here we go, this value will change on digging deeper and deeper
    	this.ctxRoot = root;
    	
    	// we need to remember where we started initially
    	this.initialRoot = root;
    }
    
    private void readContext() {
        try {
        	
            NamingEnumeration<NameClassPair> names = ctx.list(ctxRoot);

            // as long as we have something on this level
            while (names.hasMore()) {
                NameClassPair next = names.next();
                String name = next.getName();
                String clazzName = next.getClassName();

                int depth = 0;
                
                // do we have a sub-context? Unfortunately instanceof won't work
                if (clazzName.equals("javax.naming.Context")) {
                	depth++;
                	System.out.println(depth);
                    ctxRoot = ctxRoot + name + "/";
                    
                    // read sub-context recursively
                    this.readContext();
                    
                } else {
                    String lookupString = ctxRoot + name;
                    
                    // getting the jboss stuff out
                    if(depth == 1) {
                    	lookupString = lookupString.replace("java:jboss/exported/", "ejb:");
                    } else {
                    	lookupString = lookupString.replace("java:jboss/exported/", "ejb:/");
                    }
                    
                    LOG.debug("Caching: " + clazzName + " -> " + lookupString);
                    cache.put(clazzName, lookupString);
                }
                if (!names.hasMore())
                	ctxRoot = initialRoot;
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // checking the cache and deliver ... or throw an exception
    public String getLookupString(Class clazz) throws NamingException {
        String ret = this.cache.get(clazz.getName());

        // maybe we had a deployment which isn't cached yet ...
        if (null == ret) {
            LOG.debug(clazz.getSimpleName() + " not found in cache going for reload of cache");
            this.readContext();
            ret = this.cache.get(clazz.getName());
            if (null == ret) {
            	// ... unfortunately not
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
