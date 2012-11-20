/**
 * 
 */
package com.compot.test;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.test.AndroidTestCase;

import com.compot.Compot;
import com.compot.CompotFactory;
import com.compot.test.entities.Faculty;
import com.compot.test.entities.Person;
import com.compot.test.entities.Student;
import com.compot.test.entities.Teacher;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev</a>
 */
public abstract class CompotTest extends AndroidTestCase {

	CompotFactory compotFactory;

	List<Teacher> teachersList;
	List<Student> studentsList;

	Faculty faculty;
	Set<Teacher> teachers;
	Set<Student> students;
	Set<Person> all;

	@Override
	protected void setUp() throws Exception {
		mContext.deleteDatabase("test");
		compotFactory = new CompotFactory(mContext, "test");

		generateTestData();
	}

	@Override
	protected void tearDown() throws Exception {
		compotFactory.close();
	}

	protected void generateTestData() {
		faculty = new Faculty();
		studentsList = Arrays.asList(
			new Student("Vesko", 23, faculty.id, "123456", true),
			new Student("Jivko", 14, faculty.id, "190384", false)
		);
		teachersList = Arrays.asList(
			new Teacher("Brügge", 50, faculty.id, "Software Engineering"),
			new Teacher("Monahov", 28, faculty.id, "Web Engineering")
		);

		students = new HashSet<Student>(studentsList);
		teachers = new HashSet<Teacher>(teachersList);
		all = new HashSet<Person>();
		all.addAll(students);
		all.addAll(teachers);
	}

	protected void storeAll() {
		Compot compot = compotFactory.getCompot();

		compot.save(faculty);
		for (Person s : all) {
			s.facultyId = faculty.id;
			compot.save(s);
		}
	}
}
