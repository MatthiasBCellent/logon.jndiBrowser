package de.cellent.test.jndi;

import java.rmi.NotBoundException;
import java.util.HashMap;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.cellent.test.fooBean.HelloWorld;

public class JNDIBrowserTest {
	
	private static JNDIBrowser browser;

	public static void main(String[] args) {
		JNDIBrowserTest.setUp();
		JNDIBrowserTest test = new JNDIBrowserTest();
		
		test.testForNwJndiName();
		test.testGetAllBindings();
		
	}
	
	public void testGetAllBindings() {
		HashMap<String, String> bindings = browser.getAllBindings();
		Set<String> keys = bindings.keySet();
		for (String key : keys) {
			System.out.println("Key: " + key + " -> Binding: " + bindings.get(key));
		}
	}

	public void testForNwJndiName() {
		
		String jndi;
		try {
			jndi = browser.getLookupString(HelloWorld.class);
			// ejb:barService_ear-0.0.1-SNAPSHOT/barService/BarServiceBean!de.cellent.test.barService.BarService
			System.out.println(jndi);	
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setUp() {
		try {
			Context ctx = new InitialContext();
			browser = (JNDIBrowser) ctx.lookup("/jndiBrowser-0.0.1-SNAPSHOT/JNDIBrowserBean!de.cellent.test.jndi.JNDIBrowser");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
