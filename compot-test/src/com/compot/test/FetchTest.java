/**
 * 
 */
package com.compot.test;

import java.util.List;

import com.compot.Compot;
import com.compot.CompotStaticModel.Person_;
import com.compot.test.entities.Faculty;
import com.compot.test.entities.Person;
import com.compot.test.entities.Student;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev</a>
 */
public class FetchTest extends CompotTest {

	public void testFetch() {
		storeAll();
		
		Compot compot = compotFactory.getCompot();

		Object obj;
		obj = compot.query(Student.class).filterEquals(Person_.NAME, "Vesko").first();
		assertEquals(students.get(0), obj);

		obj = compot.query(Person.class).filterEquals(Person_.NAME, "Vesko").first();
		assertEquals(students.get(0), obj);

		List<Person> all = compot.query(Person.class).fetch();

		int size = this.all.size();
		for (int offset = 0; offset < size; offset++) {
			for (int limit = 1; limit < size; limit++) {
				List<Person> expected = all.subList(offset, Math.min(offset + limit, size));
				List<Person> res = compot.query(Person.class).offset(offset).limit(limit).fetch();
				assertEquals(expected, res);
			}
		}
	}

	public void testFindByID() {
		storeAll();
		
		Compot compot = compotFactory.getCompot();

		Object res;

		res = compot.findById(Faculty.class, faculty.id);
		assertEquals(faculty, res);

		for (Person p : all) {
			res = compot.findById(Person.class, p.id);
			assertEquals(p, res);
		} 
	}
}
