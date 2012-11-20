package com.compot;

import com.compot.model.MetamodelFactory;
import com.compot.model.Column;
public class CompotStaticModel {

	public static class Teacher_ {
		public static final Column ID = MetamodelFactory.get(com.compot.test.entities.Teacher.class).getColumn("id");
		public static final Column SUBJECT = MetamodelFactory.get(com.compot.test.entities.Teacher.class).getColumn("subject");
	}
	public static class Student_ {
		public static final Column ENROLLED = MetamodelFactory.get(com.compot.test.entities.Student.class).getColumn("enrolled");
		public static final Column ID = MetamodelFactory.get(com.compot.test.entities.Student.class).getColumn("id");
		public static final Column STUDENTNUMBER = MetamodelFactory.get(com.compot.test.entities.Student.class).getColumn("studentNumber");
	}
	public static class Faculty_ {
		public static final Column ID = MetamodelFactory.get(com.compot.test.entities.Faculty.class).getColumn("id");
	}
	public static class Person_ {
		public static final Column AGE = MetamodelFactory.get(com.compot.test.entities.Person.class).getColumn("age");
		public static final Column FACULTYID = MetamodelFactory.get(com.compot.test.entities.Person.class).getColumn("facultyId");
		public static final Column ID = MetamodelFactory.get(com.compot.test.entities.Person.class).getColumn("id");
		public static final Column NAME = MetamodelFactory.get(com.compot.test.entities.Person.class).getColumn("name");
	}
}
