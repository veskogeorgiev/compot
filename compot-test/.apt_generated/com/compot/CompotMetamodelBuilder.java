package com.compot;

public class CompotMetamodelBuilder extends com.compot.model.MetamodelBuilder {

	public CompotMetamodelBuilder() {
		add(com.compot.test.entities.Teacher.class);
		add(com.compot.test.entities.Student.class);
		add(com.compot.test.entities.Faculty.class);
		add(com.compot.test.entities.Person.class);
	}
}
