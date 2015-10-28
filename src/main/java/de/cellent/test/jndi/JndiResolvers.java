package de.cellent.test.jndi;

import org.apache.commons.lang3.StringUtils;


/**
 * Static utility methods pertaining to {@link JndiResolver} instances.
 *  
 * @author XVPPRAME
 * @version 1.0.0.0
 * @since Nov 21, 2012 12:16:59 PM
 */
public class JndiResolvers {
    
    private static final String REMOTE = "Remote", LOCAL = "Local", BEAN = "Bean";

    public static <I, B extends I> JNDINameResolver<I, B> forNw(Class<I> interFace, Class<B> bean, ImplementationPrefix implementationPrefix, ApplicationModule applicationModuleEar, ApplicationModule applicationModuleJar) {
        return new JNDINameResolver<I, B>(interFace, bean, implementationPrefix, applicationModuleEar, applicationModuleJar);
    }

    public static <I, B extends I> JNDINameResolver<I, B> forNw(Class<I> interFace, ImplementationPrefix implementationPrefix, ApplicationModule applicationModuleEar, ApplicationModule applicationModuleJar) {
        return new JNDINameResolver<I, B>(interFace, implementationPrefix, applicationModuleEar, applicationModuleJar);
    }
    
    public static <I, B extends I> JNDINameResolver<I, B> forNw(Class<I> interFace, ImplementationPrefix implementationPrefix, ApplicationModule jointApplicationModule) {
    	return new JNDINameResolver<I, B>(interFace, implementationPrefix, jointApplicationModule);
    }
    
    public static <I, B extends I> String getNwJndiName(Class<I> interFace, Class<B> bean, ImplementationPrefix implementationPrefix, ApplicationModule applicationModuleEar, ApplicationModule applicationModuleJar) {
        return new JNDINameResolver<I, B>(interFace, bean, implementationPrefix, applicationModuleEar, applicationModuleJar).getJndiName();
    }
    
    public static <I, B extends I> String getNwJndiName(Class<I> interFace, ImplementationPrefix implementationPrefix, ApplicationModule applicationModuleEar, ApplicationModule applicationModuleJar) {
        return new JNDINameResolver<I, B>(interFace, implementationPrefix, applicationModuleEar, applicationModuleJar).getJndiName();
    }
    
    public static <I, B extends I> String getNwJndiName(Class<I> interFace, ImplementationPrefix implementationPrefix, ApplicationModule jointApplicationModule) {
    	return new JNDINameResolver<I, B>(interFace, implementationPrefix, jointApplicationModule).getJndiName();
    }
    
    public static String getPresumedBeanName(String interfaceName) {
    	return StringUtils.substringBeforeLast(interfaceName, interfaceName.endsWith(REMOTE) ? REMOTE : LOCAL) + BEAN;
    }
}
