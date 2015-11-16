package de.cellent.test.jndi;

import java.rmi.NotBoundException;
import java.util.HashMap;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.cellent.test.fooBean.HelloWorld;

public class JNDIBrowserTest {
	
	private static JNDIBrowserWrapper browser;
	
	private static DEPLOYMENT mode = DEPLOYMENT.EAR;
	private enum DEPLOYMENT {
		EAR, JAR
	}

	public static void main(String[] args) {
		JNDIBrowserTest.setUp();
		JNDIBrowserTest test = new JNDIBrowserTest();
		
//		test.testGetJndiName();
		test.testGetAllBindings();
		
	}
	
	public void testGetAllBindings() {
		HashMap<String, String> bindings = browser.getAllBindings();
		Set<String> keys = bindings.keySet();
		for (String key : keys) {
			System.out.println("Key: " + key + " -> Binding: " + bindings.get(key));
		}
	}

	public void testGetJndiName() {
		
		String jndi;
		try {
			jndi = browser.getLookupString(HelloWorld.class);
			System.out.println(jndi);	
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setUp() {
		try {
			Context ctx = new InitialContext();
			String lookup = "ejb:jndiBrowser_ear-0.0.1-SNAPSHOT/jndiBrowser/JNDIBrowserWrapperBean!de.cellent.test.jndi.JNDIBrowserWrapper";
			
			if(JNDIBrowserTest.mode == DEPLOYMENT.JAR) {
				lookup = "ejb:/jndiBrowser-0.0.1-SNAPSHOT/JNDIBrowserWrapperBean!de.cellent.test.jndi.JNDIBrowserWrapper";
			}
			
			browser = (JNDIBrowserWrapper) ctx.lookup(lookup);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
