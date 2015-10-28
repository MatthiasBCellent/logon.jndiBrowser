package de.cellent.test.jndi;

import de.cellent.test.bean.HelloWorld;

public class JNDIResolversTest {
	
	
	public static void main(String[] args) {
		JNDIResolversTest test = new JNDIResolversTest();
		test.testForNwJndiName();
	}
	
	public void testForNwJndiName() {
		
		String jndi = JndiResolvers.getNwJndiName(HelloWorld.class, null, null, null);
		// ejb:barService_ear-0.0.1-SNAPSHOT/barService/BarServiceBean!de.cellent.test.barService.BarService
		System.out.println(jndi);	
	}

}
