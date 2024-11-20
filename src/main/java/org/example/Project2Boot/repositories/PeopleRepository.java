package org.example.Project2Boot.repositories;

import org.example.Project2Boot.models.Book;
import org.example.Project2Boot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    public Person findByBooks(List<Book> books);

    public Person findByFullname(String fullname);
}
