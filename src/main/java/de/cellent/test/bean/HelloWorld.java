package de.cellent.test.bean;

import javax.ejb.Remote;

@Remote
public interface HelloWorld {
	
	String sayHello(String msg);

}
