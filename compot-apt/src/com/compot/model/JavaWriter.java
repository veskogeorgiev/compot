package com.compot.model;

import java.io.PrintWriter;

public class JavaWriter {

	private PrintWriter writer;
	private StringBuilder indent = new StringBuilder();

	public JavaWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public JavaWriter indentIn() {
		indent.append("\t");
		return this;
	}

	public JavaWriter indentOut() {
		indent.deleteCharAt(indent.length() - 1);
		return this;
	}

	public JavaWriter indentOutAndClose() {
		indent.deleteCharAt(indent.length() - 1);
		writer.print(indent);
		writer.append("}\n");
		return this;
	}

	public JavaWriter emptyLine() {
		writer.println();
		return this;
	}

	public JavaWriter writePackage(String packageName) {
		writer.print(indent);
		writer.append("package ").append(packageName).append(";\n");
		return this;
	}

	public JavaWriter writeImport(String cls) {
		writer.print(indent);
		writer.append("import ").append(cls).append(";\n");
		return this;
	}

	public JavaWriter writeClass(String className, String extendedClass, String ...modifiers) {
		writer.print(indent);
		writer.append("public");
		for (String mod : modifiers) {
			writer.append(" ").append(mod);
		}
		writer.append(" class ").append(className);

		if (extendedClass != null) {
			writer.append(" extends ").append(extendedClass);
		}
		writer.append(" {\n");
		return this;
	}

	public JavaWriter writeMethod(String name, String[] modifiers, String[] parameters) {
		writer.print(indent);
		if (modifiers != null) {
			for (String mod : modifiers) {
				writer.append(mod).append(" ");
			}
		}
		writer.append(name).append("(");

		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				writer.append(parameters[i]);
				if (i < parameters.length - 1) {
					writer.append(", ");				
				}
			}
		}
		writer.append(") {\n");
		return this;
	}

	public JavaWriter append(String str) {
		writer.print(indent);
		writer.append(str);
		return this;
	}

	public JavaWriter println(String str) {
		writer.print(indent);
		writer.println(str);
		return this;
	}
}
