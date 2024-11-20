package org.example.Project2Boot.util;


import org.example.Project2Boot.models.Person;
import org.example.Project2Boot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz); //указываем на каком классе используется валидатор
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (peopleService.getPersonByFullname(person.getFullname()).isPresent()) { //благодаря Optional вызываем метод isPresent
            errors.rejectValue("fullname", "", "Это имя уже занято.");
        }

    }
}
