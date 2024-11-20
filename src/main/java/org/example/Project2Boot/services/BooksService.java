package org.example.Project2Boot.services;


import org.example.Project2Boot.models.Book;
import org.example.Project2Boot.models.Person;
import org.example.Project2Boot.repositories.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BookRepository bookRepository;

    public BooksService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks(boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(Sort.by("year"));
        } else {
            return bookRepository.findAll();
        }

    }

    public List<Book> getAllBooks(int page, int booksPerPage, boolean sortByYear ) {
        if (sortByYear) {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        }
        return bookRepository.findAll(PageRequest.of(page,booksPerPage)).getContent();
    }


    public Book getBookById(int id) {
        Optional<Book> foundedBook = bookRepository.findById(id);
        return foundedBook.orElse(null);
    }

    @Transactional
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(int id, Book updatedBook) {
        Person owner = bookRepository.findById(id).get().getOwner();
        updatedBook.setId(id);
        updatedBook.setOwner(owner);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void assign(Person person, int id) {
        //Работает только с таким синтаксисом
        bookRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(person);
                    book.setDateOfAssignment(new Date());
                });
    }

    @Transactional
    public void release(int id) {
        //Работает только с таким синтаксисом
        bookRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setDateOfAssignment(null);
                });
    }

    public List<Book> getBooksByOwner(Person person) {
        List<Book> foundedBooks = bookRepository.findByOwner(person);
        for (Book book : foundedBooks) {
            if (isOverdue(book)) {
                book.setOverdue(true);
            } else {
                book.setOverdue(false);
            }
        }
        return foundedBooks;
    }

    public List<Book> searchBook(String startingWith) {
        List<Book> foundedBooks = bookRepository.findByTitleStartingWith(startingWith);

        return foundedBooks;
    }

    public boolean isOverdue(Book book) {
        if (book.getDateOfAssignment() == null) {
            return false;
        }
        long tenDaysInMillis = 10L * 24 * 60 * 60 * 1000;
        Date currentDate = new Date();

        if (currentDate.getTime() - book.getDateOfAssignment().getTime() > tenDaysInMillis) {
            return true;
        }
            return false;
    }
}
