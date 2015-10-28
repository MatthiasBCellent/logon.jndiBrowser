package de.cellent.test.jndi;

import java.rmi.NotBoundException;
import java.util.HashMap;

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
 *
 */
public class JNDIBrowser {

    private static final Logger LOG = Logger.getLogger(JNDIBrowser.class);
    private static final JNDIBrowser THIS = new JNDIBrowser();

    private InitialContext ctx;
    private String ctxName = "";

    private HashMap<String, String> cache = new HashMap<String, String>();

    // Singleton
    private JNDIBrowser() {
        try {
            ctx = new InitialContext();
            this.readContext();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static JNDIBrowser getInstance() {
        return THIS;
    }

    private void readContext() {
        try {
            NamingEnumeration<NameClassPair> names = ctx.list(ctxName);

            // as long as we have something on this level
            while (names.hasMore()) {
                NameClassPair next = names.next();
                String name = next.getName();
                String clazzName = next.getClassName();

                // do we have a sub-context?
                if (clazzName.equals("javax.naming.Context")) {
                	
                    // this first-slash issue if no ear is present
                    if (ctxName.equals("")) {
                        ctxName = name;
                    } else {
                        ctxName = ctxName + "/" + name;
                    }
                    // read sub-context recursively
                    this.readContext();
                    
                } else {
                    String lookupString = ("ejb:" + ctxName + "/" + name);
                    LOG.debug("Caching: " + clazzName + " -> " + lookupString);
                    cache.put(clazzName, lookupString);
                }
                if (!names.hasMore())
                    ctxName = "";
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // checking the cache and deliver ... or throw an exception
    public String getLookupString(Class clazz) throws NotBoundException {
        String ret = this.cache.get(clazz.getName());

        // maybe we had a deployment which isn't cached yet
        if (null == ret) {
            LOG.debug(clazz.getSimpleName() + " not found in cache going for reload of cache");
            this.readContext();
            ret = this.cache.get(clazz.getName());
            if (null == ret) {
                throw new NotBoundException(clazz.getSimpleName() + " not bound to JNDI");
            }
        }
        return ret;
    }
}
