Compot is a ORM framework for Adnroid which supports inheritance. It uses annotations to define entities and APT for automatic entity scanning.

Latest version: beta-0.1 supports:
  * Automatic table creation form entity classes:
  * **Entity Inheritance**
  * Compile time static model generation
  * Insert, Delete, Update
  * Query by filtering object keys

Compot lets you start with your application logic right away. No boilerplate code!

```
@Entity
public class Person {
  @Id
  Long id;
  @MaxLength(value = 128)
  String name;
  int age;

  @Foreign(entity = Faculty.class, property = "id")
  Long facultyId;

  // getters and setters
  ...
}

@Entity
public class Student extends Person {
  @Id
  Long id;
  @Unique
  @MaxLength(value = 128)
  String studentNumber;
  boolean enrolled;

  // getters and setters
  ...
}
...
CompotFactory compotFactory = new CompotFactory(myAndroidContext, "myDatabaseName");
Compot compot = compotFactory.getCompot();

Student student = new Student(...);
compot.save(student);
...
List<Student> res = compot.query(Student.class).equals(Person_.NAME, "John").fetch();
...
```
