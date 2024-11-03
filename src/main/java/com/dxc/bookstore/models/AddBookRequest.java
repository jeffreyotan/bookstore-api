package com.dxc.bookstore.models;

import java.util.List;

public class AddBookRequest {
    private String isbn;
    private String title;
    private List<AddAuthorRequest> authors;
    private int year;
    private double price;
    private String genre;
    
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<AddAuthorRequest> getAuthors() {
        return authors;
    }
    public void setAuthors(List<AddAuthorRequest> authors) {
        this.authors = authors;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    
}
