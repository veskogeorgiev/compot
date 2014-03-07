package com.compot.model;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@SupportedAnnotationTypes("com.compot.annotations.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class StaticMetamodelAnnotationProcessor extends CompotAnnotationProcessor {

    @Override
	protected void generateSources(List<Element> entities) throws IOException {
		JavaFileObject jfo = env.getFiler().createSourceFile(SM_CLASS_NAME);
		PrintWriter writer = new PrintWriter(jfo.openWriter());
		JavaWriter jw = new JavaWriter(writer);

		jw.writePackage(PACKAGE_NAME).emptyLine();
		jw.writeImport("com.compot.model.MetamodelFactory");
		jw.writeImport("com.compot.model.Column");
		jw.writeClass(SM_SIMPLE_CLASS_NAME, null).emptyLine();

		jw.indentIn();

		for (Element entityEl : entities) {
			jw.writeClass(entityEl.getSimpleName().toString() + "_", null, "static");
			jw.indentIn();

			for (Element fieldEl : entityEl.getEnclosedElements()) {
				if (fieldEl.getKind().equals(ElementKind.FIELD)) {
					jw.println(getColumnField(entityEl, fieldEl));
				}
			}
			jw.indentOutAndClose();
		}
		jw.indentOutAndClose();
		writer.close();
	}

	private String getColumnField(Element entityEl, Element fieldEl) {
		String name = fieldEl.getSimpleName().toString();
		StringBuilder ret = new StringBuilder();
		ret.append("public static final Column ").append(name.toUpperCase()).append(" = ");
		ret.append("MetamodelFactory.get(").append(entityEl.toString()).append(".class)");
		ret.append(".getColumn(\"").append(name).append("\");");
		return ret.toString();
	}
}
