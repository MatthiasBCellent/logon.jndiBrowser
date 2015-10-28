package de.cellent.test.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class PrintJNDITree {
	
	private int indentLevel = 0;
	private Context context = null;

	public static void main(String[] args) throws Exception {
		new PrintJNDITree().printJNDITree("");
		System.out.println("DONE");
	}

	public PrintJNDITree() throws NamingException {
		context = new InitialContext();
	}

	private void printNamingEnumeration(NamingEnumeration ne, String parentctx)
			throws NamingException {
		System.out.println("Current context: " + parentctx);
		while (ne.hasMoreElements()) {
			NameClassPair next = (NameClassPair) ne.nextElement();
			System.out.println("Classname: " + next.getClassName());
			printEntry(next);
			increaseIndent();
			if(parentctx.equals("")) {
				this.printJNDITree(next.getName());
			} else {
				this.printJNDITree(parentctx + "/" + next.getName());
			}
			decreaseIndent();
		}
	}
	
	public void printJNDITree(String ct) {
		try {
			printNamingEnumeration(context.list(ct), ct);
		} catch (NamingException e) {
			// ignore leaf node exception
		}
	}

	private void printEntry(NameClassPair next) {
		System.out.println(printIndent() + "-->" + next);
	}

	private void increaseIndent() {
		indentLevel += 4;
	}

	private void decreaseIndent() {
		indentLevel -= 4;
	}

	private String printIndent() {
		StringBuffer buf = new StringBuffer(indentLevel);
		for (int i = 0; i < indentLevel; i++) {
			buf.append(" ");
		}
		return buf.toString();
	}
}
