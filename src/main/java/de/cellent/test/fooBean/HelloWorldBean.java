package de.cellent.test.fooBean;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(HelloWorld.class)
@Local(HelloWorldLocal.class)
public class HelloWorldBean implements HelloWorld {

	@Override
	public String sayHello(String msg) {
		
		return this.getClass().getSimpleName() + " replied: " + msg.toUpperCase();
	}

}
