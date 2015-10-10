package com.qpark.maven.xmlbeans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bhausen
 */
public class MethodDefinition {
	private ComplexType out;
	private final List<ComplexTypeChild> input = new ArrayList<ComplexTypeChild>();

	public MethodDefinition() {
	}

	public MethodDefinition(final ComplexTypeChild in, final ComplexType out) {
		this.input.add(in);
		this.out = out;
	}

	public MethodDefinition(final ComplexType out) {
		this.out = out;
	}

	public MethodDefinition(final ComplexTypeChild in) {
		this.input.add(in);
	}

	/**
	 * @return the out
	 */
	public ComplexType getOut() {
		return this.out;
	}

	/**
	 * @param out the out to set
	 */
	public void setOut(final ComplexType out) {
		this.out = out;
	}

	/**
	 * @return the input
	 */
	public List<ComplexTypeChild> getInput() {
		return this.input;
	}
}
