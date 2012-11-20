package com.compot.test;

import java.util.List;

import com.compot.Compot;
import com.compot.CompotStaticModel.Person_;
import com.compot.test.entities.Student;

public class UpdateTest extends CompotTest {

	public void testUpdate() {
		storeAll();

		Compot compot = compotFactory.getCompot();

		Student s = new Student("Test", 23, faculty.id, "12345", false);
		compot.save(s);

		s.enrolled = true;
		compot.save(s);

		List<Student> res = compot.query(Student.class).filterEquals(Person_.NAME, "Test").fetch();
		assertEquals(1, res.size());
		assertEquals(s, res.get(0));
	}

}
