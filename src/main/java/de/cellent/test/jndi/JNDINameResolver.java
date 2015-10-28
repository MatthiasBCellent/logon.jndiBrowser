package de.cellent.test.jndi;

import java.rmi.NotBoundException;


public class JNDINameResolver<I, B extends I> implements JndiResolver {

	private final ImplementationPrefix implementationPrefix;
	private final ApplicationModule applicationModuleEar;
	private final ApplicationModule applicationModuleJar;
	private final Class<I> interfaceClass;
	private Class<B> beanClass;
	
	private JNDIBrowser browser = JNDIBrowser.getInstance();

	/**
	 * Constructor of NwJndiResolver
	 * 
	 * @param beanClass
	 * @param interfaceClass
	 */
	public JNDINameResolver(Class<I> interfaceClass, Class<B> beanClass,
			ImplementationPrefix implementationPrefix,
			ApplicationModule applicationModuleEar,
			ApplicationModule applicationModuleJar) {
		this(interfaceClass, implementationPrefix, applicationModuleEar,
				applicationModuleJar);
		this.beanClass = beanClass;
	}

	public JNDINameResolver(Class<I> interfaceClass,
			ImplementationPrefix implementationPrefix,
			ApplicationModule jointApplicationModule) {
		this(interfaceClass, implementationPrefix, jointApplicationModule,
				jointApplicationModule);
	}

	public JNDINameResolver(Class<I> interfaceClass,
			ImplementationPrefix implementationPrefix,
			ApplicationModule applicationModuleEar,
			ApplicationModule applicationModuleJar) {
		this.implementationPrefix = implementationPrefix;
		this.applicationModuleEar = applicationModuleEar;
		this.applicationModuleJar = applicationModuleJar;
		this.interfaceClass = interfaceClass;
	}

	@Override
	public String getJndiName() {
		String ret = null;
		try {
			ret = this.browser.getLookupString(this.interfaceClass);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return ret;
	}

}
