package com.compot.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(value = {"com.compot.annotations.Entity"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class StaticMetamodelAnnotationProcessor extends CompotAnnotationProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		try {
			generateStaticModel(annotations, roundEnv);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return false;
	}

	private void generateStaticModel(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws IOException {
		JavaFileObject jfo = env.getFiler().createSourceFile(SM_CLASS_NAME);
		PrintWriter writer = new PrintWriter(jfo.openWriter());
		JavaWriter jw = new JavaWriter(writer);

		jw.writePackage(PACKAGE_NAME).emptyLine();
		jw.writeImport("com.compot.model.MetamodelFactory");
		jw.writeImport("com.compot.model.Column");
		jw.writeClass(SM_SIMPLE_CLASS_NAME, null).emptyLine();

		jw.indentIn();

		for (Element entityEl : getEntityElements(annotations, roundEnv)) {
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
