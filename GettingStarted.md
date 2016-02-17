# Introduction #

This page will help you to get started with Compot

# Setup #

To use Compot in your project add latest version of compot-core.jar to your build path. You also need to enable compile time APT on your project and add latest version of compot-apt.jar in the factory path of the APT. On compile 2 source files will be generated (in .apt\_generated/ if you use standard eclipse setup).

# Entities #

With Compot  you create your model JPA style with annotations. e.g
```
import com.compot.annotations.*;

@Entity
public class Faculty {
  @Id
  Long id;
  String name;

  // getters and setters
  ...
}

---

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

---

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
```

You don't have to register you entity classes anywhere because compot-apt does that for you. It also generates a static model class - com`.`compot`.`CompotStaticModule which contains a static model with the columns of your entities. You can use later use this static model to query objects by key. The static model for each entity class has the same name with appended "_" at the end e.g. for Person.class it is Person_.class.

So far in our demo entities the generated static model looks like this:
```
public class CompotStaticModel {
  public static class Student_ {
    public static final Column ENROLLED = MetamodelFactory.get(com.compot.test.entities.Student.class).getColumn("enrolled");
    public static final Column ID = MetamodelFactory.get(com.compot.test.entities.Student.class).getColumn("id");
    public static final Column STUDENTNUMBER = MetamodelFactory.get(com.compot.test.entities.Student.class).getColumn("studentNumber");
  }
  public static class Faculty_ {
    public static final Column ID = MetamodelFactory.get(com.compot.test.entities.Faculty.class).getColumn("id");
  }
  public static class Person_ {
    public static final Column AGE = MetamodelFactory.get(com.compot.test.entities.Person.class).getColumn("age");
    public static final Column FACULTYID = MetamodelFactory.get(com.compot.test.entities.Person.class).getColumn("facultyId");
    public static final Column ID = MetamodelFactory.get(com.compot.test.entities.Person.class).getColumn("id");
    public static final Column NAME = MetamodelFactory.get(com.compot.test.entities.Person.class).getColumn("name");
  }
}

```

Note the _facultyId_ property in _Person_ - for now Compot does not support automatic column join.

It is also not required that your entities have getters and setters, because Compot does not use them.

# The Compot object #

Your main data access object is com.compot.Compot. You can obtain one from com.compot.CompotFactory.

```
CompotFactory compotFactory = new CompotFactory(myAndroidContext, "myDatabaseName");
Compot compot = compotFactory.getCompot();
```

# Saving objects #

To save an object simply call compot.save(). If the the object does not have an id, it will saved, and one will be generated and set to the object. If it already contains an id, then Compot will try to find it in the database and updated the database record with the new values.

```
Student student = new Student(....);
compot.save(student);
```

# Querying objects #

Querying for objects is done by the com.compoy.dao.Query interface.

```
List<Person> result = compot.query(Person.class).equals(Person_.NAME, "personName").list();
```

Because Compot is focused on entity inheritance, this result list is generic by Person.class but it will contain instances of their actual type. e.g. if you have saved only students in the database, the result will contain Student objects, not Persons

# Delete objects #

Deleting objects is just as easy:

```
boolean actuallyDeleated = compot.delete(student);
```