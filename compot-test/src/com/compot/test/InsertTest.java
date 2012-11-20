/**
 * 
 */
package com.compot.test;

import java.util.HashSet;
import java.util.Set;

import com.compot.Compot;
import com.compot.test.entities.Person;
import com.compot.test.entities.Student;
import com.compot.test.entities.Teacher;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev</a>
 */
public class InsertTest extends CompotTest {

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void testInsert() {
		Compot compot = compotFactory.getCompot();

		compot.save(faculty);

		for (Student s : students) {
			s.facultyId = faculty.id;
			compot.save(s);
		}

		Set<?> set;
		set = new HashSet(compot.query(Teacher.class).fetch());
		assertTrue(set.isEmpty());

		set = new HashSet(compot.query(Student.class).fetch());
		assertTrue(set.equals(students));

		set = new HashSet(compot.query(Person.class).fetch());
		assertTrue(set.equals(students));

		for (Teacher t : teachers) {
			t.facultyId = faculty.id;
			compot.save(t);
		}
		set = new HashSet(compot.query(Student.class).fetch());
		assertTrue(set.equals(students));

		set = new HashSet(compot.query(Teacher.class).fetch());
		assertTrue(set.equals(teachers));

		set = new HashSet(compot.query(Person.class).fetch());
		assertTrue(set.equals(all));
	}

}
