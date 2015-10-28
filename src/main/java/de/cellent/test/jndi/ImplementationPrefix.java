package de.cellent.test.jndi; 

/**
 * JBoss doesn't need this
 * 
 * @author mbohnen
 *
 */
public enum ImplementationPrefix {
	
	JAVAEE("JavaEE"), 
	SAP_COM("sap.com"),
	ZEISS_ORG("vision.zeiss.org");

	private String prefix;

	private ImplementationPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}
	
}