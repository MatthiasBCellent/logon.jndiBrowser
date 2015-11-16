package de.cellent.test.jndi;

import java.rmi.NotBoundException;
import java.util.HashMap;

import javax.ejb.Remote;

@Remote
public interface JNDIBrowserWrapper {

	String getLookupString(Class clazz) throws NotBoundException;
	HashMap<String, String> getAllBindings();
	<T> T getStub(Class<T> clazz);
}
