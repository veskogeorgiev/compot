package com.compot.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public abstract class CompotAnnotationProcessor extends AbstractProcessor {

	static final String PACKAGE_NAME = "com.compot";

	static final String SM_SIMPLE_CLASS_NAME = "CompotStaticModel";
	static final String SM_CLASS_NAME = PACKAGE_NAME + "." + SM_SIMPLE_CLASS_NAME;

	static final String BOOTSTRAP_SIMPLE_CLASS_NAME = "CompotMetamodelBuilder";
	static final String BOOTSTRAP_CLASS_NAME = PACKAGE_NAME + "." + BOOTSTRAP_SIMPLE_CLASS_NAME;

	protected ProcessingEnvironment env;

	@Override
	public void init(ProcessingEnvironment processingEnv) {
		this.env = processingEnv;
	}

	protected List<Element> getEntityElements(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws IOException {
		List<Element> entities = new LinkedList<Element>();
		for (TypeElement ann : annotations) {
			for (Element el : roundEnv.getElementsAnnotatedWith(ann)) {
				entities.add(el);
			}
		}
		return entities;
	}
}
