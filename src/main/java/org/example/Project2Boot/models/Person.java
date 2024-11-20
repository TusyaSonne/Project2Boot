package org.example.Project2Boot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotEmpty(message = "ФИО не может быть пустым")
    @Pattern(regexp = "[А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+", message = "Введите корректное ФИО (Иванов Иван Иванович")
    @Column(name = "fullname")
    private String fullname;

    //@NotEmpty(message = "Введите год рождения")
    @Min(value = 1900, message = "Год рождения не может быть позже 20 века")
    @Max(value = 2024, message = "Год рождения не может быть раньше 2024 года")
    @Column(name = "birthyear")
    private int birthyear;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Book> books;

    public Person(int id, String fullname, int birthyear) {
        this.id = id;
        this.fullname = fullname;
        this.birthyear = birthyear;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
