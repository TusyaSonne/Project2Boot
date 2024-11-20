package org.example.Project2Boot.controllers;

import jakarta.validation.Valid;

import org.example.Project2Boot.models.Person;
import org.example.Project2Boot.services.BooksService;
import org.example.Project2Boot.services.PeopleService;
import org.example.Project2Boot.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    private final BooksService booksService;


    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator, BooksService booksService) {

        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.booksService = booksService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.getAllPersons());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) { //запомнить синтаксис
        model.addAttribute("person", peopleService.getPersonById(id));
        model.addAttribute("books", booksService.getBooksByOwner(peopleService.getPersonById(id)));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/new";
        }

        peopleService.savePerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.getPersonById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        peopleService.updatePerson(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.deletePerson(id);
        return "redirect:/people";
    }
}

    //@GetMapping("/search")
    //public String search(@RequestParam(""))

