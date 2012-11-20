/**
 * 
 */
package com.compot.test;

import com.compot.Compot;
import com.compot.test.entities.Person;
import com.compot.test.entities.Student;
import com.compot.test.entities.Teacher;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev</a>
 */
public class InsertTest extends CompotTest {

	public void testInsert() {
		Compot compot = compotFactory.getCompot();

		compot.save(faculty);

		for (Student s : students) {
			s.facultyId = faculty.id;
			compot.save(s);
		}
		assertTrue(compot.query(Teacher.class).fetch().isEmpty());

		assertSame(students, compot.query(Student.class).fetch());
		assertSame(students, compot.query(Person.class).fetch());

		for (Teacher t : teachers) {
			t.facultyId = faculty.id;
			compot.save(t);
		}
		assertSame(students, compot.query(Student.class).fetch());
		assertSame(teachers, compot.query(Teacher.class).fetch());
		assertSame(all, compot.query(Person.class).fetch());
	}

}
