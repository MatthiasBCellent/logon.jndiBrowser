package de.cellent.test.jndi;


/**
 * Interface for resolving JNDI name.
 * @see NwJndiResolver (later we might have JbossJndiResolver).
 *  
 * @author XVPPRAME
 * @version 1.0.0.0
 * @since Nov 21, 2012 12:12:49 PM
 */
public interface JndiResolver {
    
    String getJndiName();

}
