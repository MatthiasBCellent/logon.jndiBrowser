package de.cellent.test.fooBean;

import javax.ejb.Remote;

@Remote
public interface HelloWorld {
	
	String sayHello(String msg);

}
