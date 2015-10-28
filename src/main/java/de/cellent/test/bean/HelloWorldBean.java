package de.cellent.test.bean;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(HelloWorld.class)
public class HelloWorldBean implements HelloWorld {

	@Override
	public String sayHello(String msg) {
		
		return this.getClass().getSimpleName() + " replied: " + msg.toUpperCase();
	}

}
