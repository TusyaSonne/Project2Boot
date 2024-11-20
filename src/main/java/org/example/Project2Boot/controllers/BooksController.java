package org.example.Project2Boot.controllers;

import jakarta.validation.Valid;

import org.example.Project2Boot.models.Book;
import org.example.Project2Boot.models.Person;
import org.example.Project2Boot.services.BooksService;
import org.example.Project2Boot.services.PeopleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final PeopleService peopleService;
    private final BooksService booksService;

    public BooksController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }


    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sorted_by_year", required = false) boolean sortedByYear) {

        if (booksPerPage == null || page == null) {
            model.addAttribute("books", booksService.getAllBooks(sortedByYear));
        } else {
            model.addAttribute("books", booksService.getAllBooks(page, booksPerPage, sortedByYear));
        }

        return "books/index";

    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {

        model.addAttribute("book", booksService.getBookById(id));
        //Optional<Person> bookOwner = bookDAO.getBookOwner(id);
        Optional<Person> bookOwner = peopleService.getBookOwner(id);

        if (bookOwner.isPresent()) {
            model.addAttribute("owner", bookOwner.get());
        } else {
            model.addAttribute("people", peopleService.getAllPersons());
        }

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) { //если мы используем thymeleaf-формы, им необходимо передавать тот объект, для которого эта форма нужна
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        booksService.addBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.getBookById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) { //ВСЕГДА после Valid BindingResult

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        booksService.updateBook(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.deleteBook(id);
        return "redirect:/books";
    }


    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int book_id, @ModelAttribute("person") Person person) {
        booksService.assign(person, book_id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int book_id) {
        booksService.release(book_id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchPage() {

        return "books/search";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam("query") String found_request) {
        model.addAttribute("books", booksService.searchBook(found_request));

        return "books/search";
    }

}
