/**
 * 
 */
package com.compot.test.entities;

import com.compot.annotations.Entity;
import com.compot.annotations.Id;
import com.compot.annotations.MaxLength;
import com.compot.annotations.Unique;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev</a>
 */
@Entity
public class Student extends Person {

	@Id
	public Long id;
	@Unique
	@MaxLength(value = 128)
	public String studentNumber;
	public boolean enrolled;

	public Student() {

	}

	public Student(String name, int age, Long facultyId, String studentNumber, boolean enrolled) {
		super(name, age, facultyId);
		this.studentNumber = studentNumber;
		this.enrolled = enrolled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (enrolled ? 1231 : 1237);
		result = prime * result + ((studentNumber == null) ? 0 : studentNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (enrolled != other.enrolled)
			return false;
		if (studentNumber == null) {
			if (other.studentNumber != null)
				return false;
		}
		else if (!studentNumber.equals(other.studentNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", studentNumber=" + studentNumber + ", enrolled=" + enrolled + ", name=" + name + ", age=" + age + ", facultyId=" + facultyId + "]";
	}

	
}
