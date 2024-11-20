package org.example.Project2Boot.services;

import org.example.Project2Boot.models.Person;
import org.example.Project2Boot.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BooksService booksService;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksService booksService) {
        this.peopleRepository = peopleRepository;
        this.booksService = booksService;
    }

    public List<Person> getAllPersons() {
        return peopleRepository.findAll();
    }

    public Person getPersonById(int id) {

        Optional<Person> foundedPerson = peopleRepository.findById(id);

        return foundedPerson.orElse(null);
    }

    @Transactional
    public void savePerson(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void deletePerson(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getBookOwner(int id) {
        Optional<Person> foundedPerson = Optional.ofNullable(peopleRepository.findByBooks(new ArrayList<>(Collections.singletonList(booksService.getBookById(id)))));
        return foundedPerson;
    }

    public Optional<Person> getPersonByFullname(String fullname) {
        Optional<Person> foundedPerson = Optional.ofNullable(peopleRepository.findByFullname(fullname));
        return foundedPerson;
    }
}
