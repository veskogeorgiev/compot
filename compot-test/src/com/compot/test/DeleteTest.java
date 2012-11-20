//package com.compot.test;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import com.compot.Compot;
//import com.compot.test.entities.Person;
//import com.compot.test.entities.Student;
//import com.compot.test.entities.Teacher;
//
//public class DeleteTest extends CompotTest {
//
//	public void testDelete() {
//		Compot compot = compotFactory.getCompot();
//		compot.save(faculty);
//		for (Person s : all) {
//			s.facultyId = faculty.id;
//			compot.save(s);
//		}
//
//		List<Student> modStudents = new LinkedList<Student>(students);
//		List<Teacher> modTeachers = new LinkedList<Teacher>(teachers);
//		List<Person> modAll = new LinkedList<Person>(all);
//
//		modStudents.remove(studentsList.get(0));
//		modAll.remove(studentsList.get(0));
//		compot.delete(studentsList.get(0));
//		assertEquals(modAll, compot.query(Person.class).fetch());
//		assertEquals(modTeachers, compot.query(Teacher.class).fetch());
//		assertEquals(modStudents, compot.query(Student.class).fetch());
//
//		modTeachers.remove(teachersList.get(0));
//		modAll.remove(teachersList.get(0));
//		compot.delete(teachersList.get(0));
//
//		assertEquals(modAll, compot.query(Person.class).fetch());
//		assertEquals(modTeachers, compot.query(Teacher.class).fetch());
//		assertEquals(modStudents, compot.query(Student.class).fetch());
//	}
//
//}
