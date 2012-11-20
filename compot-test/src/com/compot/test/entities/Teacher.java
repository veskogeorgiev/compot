/**
 * 
 */
package com.compot.test.entities;

import com.compot.annotations.Entity;
import com.compot.annotations.Id;
import com.compot.annotations.MaxLength;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev</a>
 */
@Entity
public class Teacher extends Person {

	@Id
	public Long id;
	@MaxLength(value = 128)
	public String subject;

	public Teacher() {

	}

	public Teacher(String name, int age, Long facultyId, String subject) {
		super(name, age, facultyId);
		this.subject = subject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		Teacher other = (Teacher) obj;
		if (subject == null) {
			if (other.subject != null)
				return false;
		}
		else if (!subject.equals(other.subject))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Teacher [id=" + id + ", subject=" + subject + ", name=" + name + ", age=" + age + ", facultyId=" + facultyId + "]";
	}

}
