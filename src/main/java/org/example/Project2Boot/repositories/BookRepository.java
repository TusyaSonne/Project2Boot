package org.example.Project2Boot.repositories;


import org.example.Project2Boot.models.Book;
import org.example.Project2Boot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    public List<Book> findByOwner(Person owner);

    public List<Book> findByTitleStartingWith(String startingWith);
}
