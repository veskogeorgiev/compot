package com.compot.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(value = {"com.compot.annotations.Entity"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MetamodelBuilderAnnotationProcessor extends CompotAnnotationProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		try {
			List<Element> entities = getEntityElements(annotations, roundEnv);
			generateBootstrapBuilder(entities);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	private void generateBootstrapBuilder(List<Element> entities) throws IOException {
		JavaFileObject jfo = env.getFiler().createSourceFile(BOOTSTRAP_CLASS_NAME);
		PrintWriter writer = new PrintWriter(jfo.openWriter());
		JavaWriter jw = new JavaWriter(writer);
	
		jw.writePackage(PACKAGE_NAME).emptyLine();

		jw.writeClass(BOOTSTRAP_SIMPLE_CLASS_NAME, "com.compot.model.MetamodelBuilder").emptyLine();

		jw.indentIn();
		// constructor
		jw.writeMethod(BOOTSTRAP_SIMPLE_CLASS_NAME, new String[] {"public"}, null);
		jw.indentIn();

		for (Element el : entities) {
			String line = "add(" + el + ".class);";
			jw.println(line);
		}
		// end constructor
		jw.indentOutAndClose();
		jw.indentOutAndClose();

		writer.close();
	}

}
