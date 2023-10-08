package com.kontial.cloud.service.cloudservice.persistence;

import com.kontial.cloud.service.cloudservice.model.Person;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryDataSource {

    private static List<Person> persons;

    static {
        persons = new ArrayList<>();
        persons.add(new Person("h2314", "Thomas", LocalDate.of(1980, 6, 4)));
        persons.add(new Person("f5962", "Thomas", LocalDate.of(1967, 10, 16)));
        persons.add(new Person("e5891", "Evelin", LocalDate.of(1942, 1, 5)));
        persons.add(new Person("t7811", "Oliver", LocalDate.of(1992, 8, 9)));
        persons.add(new Person("z5894", "Oliver", LocalDate.of(1962, 1, 31)));
        persons.add(new Person("s8971", "Oliver", LocalDate.of(1989, 2, 9)));
        persons.add(new Person("u5841", "Oliver", LocalDate.of(1985, 2, 10)));
        persons.add(new Person("n2361", "Jennifer", LocalDate.of(1992, 4, 8)));
        persons.add(new Person("w2054", "John", LocalDate.of(1988, 7, 8)));
        persons.add(new Person("x9815", "Mike", LocalDate.of(1969, 8, 1)));
        persons.add(new Person("c6358", "Henry", LocalDate.of(1998, 6, 30)));
        persons.add(new Person("a2601", "Lucas", LocalDate.of(1993, 4, 11)));
        persons.add(new Person("e8450", "Alice", LocalDate.of(1969, 6, 9)));
        persons.add(new Person("w9640", "Alice", LocalDate.of(2002, 5, 18)));
        persons.add(new Person("e5036", "Alice", LocalDate.of(2008, 3, 19)));
        persons.add(new Person("t8405", "Andrea", LocalDate.of(2011, 10, 4)));
        persons.add(new Person("u7840", "Ava", LocalDate.of(1991, 7, 30)));
        persons.add(new Person("i6922", "Ava", LocalDate.of(1990, 11, 9)));
    }

    public List<Person> getAll() {
        return persons;
    }

    public void addPerson(Person person) {
        persons.add(person);
    }

    public boolean isPersonIdUnique(String id) {
        return persons.stream().noneMatch(p -> p.id().equals(id));
    }
}
