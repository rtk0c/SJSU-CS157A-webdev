package com.model;

import java.sql.Date;

public class Books {
    private int book_id;
    private String title;
    private String author;
    private int page_count;
    private String publisher;
    private Date date_published;
    private int library_copies;
    private int available_copies;


    public Books()
    {

    }

    public Books(int book_id, String title, String author, int page_count, String publisher, Date date_published, int library_copies, int available_copies) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.page_count = page_count;
        this.publisher = publisher;
        this.date_published = date_published;
        this.library_copies = library_copies;
        this.available_copies = available_copies;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getDate_published() {
        return date_published;
    }

    public void setDate_published(Date date_published) {
        this.date_published = date_published;
    }

    public int getLibrary_copies() {
        return library_copies;
    }

    public void setLibrary_copies(int library_copies) {
        this.library_copies = library_copies;
    }

    public int getAvailable_copies() {
        return available_copies;
    }

    public void setAvailable_copies(int available_copies) {
        this.available_copies = available_copies;
    }
}
