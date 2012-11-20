package com.compot.test;

import java.util.HashSet;
import java.util.Set;

import com.compot.Compot;
import com.compot.test.entities.Person;
import com.compot.test.entities.Student;
import com.compot.test.entities.Teacher;

public class DeleteTest extends CompotTest {

	public void testDelete() {
		Compot compot = compotFactory.getCompot();
		compot.save(faculty);
		for (Person s : all) {
			s.facultyId = faculty.id;
			compot.save(s);
		}

		Set<Student> modStudents = new HashSet<Student>(students);
		Set<Teacher> modTeachers = new HashSet<Teacher>(teachers);
		Set<Person> modAll = new HashSet<Person>(all);

		modStudents.remove(students.get(0));
		modAll.remove(students.get(0));
		compot.delete(students.get(0));

		assertSame(modAll, compot.query(Person.class).fetch());
		assertSame(modTeachers, compot.query(Teacher.class).fetch());
		assertSame(modStudents, compot.query(Student.class).fetch());

		modTeachers.remove(teachers.get(0));
		modAll.remove(teachers.get(0));
		compot.delete(teachers.get(0));

		assertSame(modAll, compot.query(Person.class).fetch());
		assertSame(modTeachers, compot.query(Teacher.class).fetch());
		assertSame(modStudents, compot.query(Student.class).fetch());
	}

}
