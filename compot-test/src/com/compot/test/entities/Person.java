/**
 * 
 */
package com.compot.test.entities;

import com.compot.annotations.Entity;
import com.compot.annotations.Foreign;
import com.compot.annotations.Id;
import com.compot.annotations.MaxLength;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev</a>
 */
@Entity
public abstract class Person {

	@Id
	public Long id;
	@MaxLength(value = 128)
	public String name;
	public int age;

	@Foreign(entity = Faculty.class, property = "id")
	public Long facultyId;

	public Person() {

	}

	public Person(String name, int age, Long facultyId) {
		this.name = name;
		this.age = age;
		this.facultyId = facultyId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((facultyId == null) ? 0 : facultyId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (age != other.age)
			return false;
		if (facultyId == null) {
			if (other.facultyId != null)
				return false;
		}
		else if (!facultyId.equals(other.facultyId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", age=" + age + ", facultyId=" + facultyId + "]";
	}

}
