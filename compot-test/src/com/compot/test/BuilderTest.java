package com.compot.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.test.AndroidTestCase;

import com.compot.model.MetamodelBuilder;
import com.compot.test.entities.Faculty;
import com.compot.test.entities.Person;
import com.compot.test.entities.Student;
import com.compot.test.entities.Teacher;

public class BuilderTest extends AndroidTestCase {

	@SuppressWarnings("unchecked")
	public void testBuildModel() {
		Map<Class<?>, Collection<Class<?>>> expected = new HashMap<Class<?>, Collection<Class<?>>>();
		expected.put(Faculty.class, new LinkedList<Class<?>>());
		expected.put(Person.class, new LinkedList<Class<?>>(Arrays.asList(Student.class, Teacher.class)));
		expected.put(Student.class, new LinkedList<Class<?>>());
		expected.put(Teacher.class, new LinkedList<Class<?>>());

		MetamodelBuilder mb = new MetamodelBuilder() {};
		mb.add(Faculty.class).add(Person.class).add(Student.class).add(Teacher.class);

		assertEquals(expected, mb.getHierarchy());
	}

}
