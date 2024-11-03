package com.dxc.bookstore.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dxc.bookstore.converters.BookDetailsConverter;
import com.dxc.bookstore.entity.Author;
import com.dxc.bookstore.entity.Book;
import com.dxc.bookstore.models.AddBookRequest;
import com.dxc.bookstore.models.BookDetails;
import com.dxc.bookstore.service.BookstoreService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class BookstoreController {
    private BookstoreService bookstoreService;
    private BookDetailsConverter bookDetailsConverter;

    public BookstoreController(BookstoreService bookstoreService,
                               BookDetailsConverter bookDetailsConverter) {
        this.bookstoreService = bookstoreService;
        this.bookDetailsConverter = bookDetailsConverter;
    }

    @GetMapping("/author")
    public ResponseEntity<List<Author>> getAuthors(@RequestParam(value = "name", required = false) String name) {
        List<Author> authors = new ArrayList<>();
        if (name != null) {
            authors = bookstoreService.getAuthor(name);
        } else {
            authors = bookstoreService.getAuthors();
        }
        ResponseEntity<List<Author>> response = new ResponseEntity<>(authors, HttpStatus.OK);
        return response;
    }
    
    @GetMapping("/book")
    public ResponseEntity<List<BookDetails>> getBook(
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "author", required = false) String author
    ) {
        List<Book> books = new ArrayList<>();
        if (title == null) {
            if (author == null) {
                books = bookstoreService.getAllBooks();
            } else {
                books = bookstoreService.getBooksByAuthor(author);
            }
        } else {
            if (author == null) {
                books = bookstoreService.getBooksByTitle(title);
            } else {
                books = bookstoreService.getBooksByAuthorAndTitle(title, author);
            }
        }
        List<BookDetails> bookDetailsList = books.stream().map(book -> bookDetailsConverter.convert(book)).toList();
        ResponseEntity<List<BookDetails>> response = new ResponseEntity<>(bookDetailsList, HttpStatus.OK);
        return response;
    }

    @PostMapping("/book")
    public ResponseEntity<List<BookDetails>> addBook(@RequestBody AddBookRequest request) {
        ResponseEntity<List<BookDetails>> response = null;
        if (request.getIsbn() == null || bookstoreService.checkIfBookIsInDB(request.getIsbn())) {
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            List<Book> books = bookstoreService.addBook(request);
            List<BookDetails> bookDetailsList = books.stream().map(book -> bookDetailsConverter.convert(book)).toList();
            response = new ResponseEntity<>(bookDetailsList, HttpStatus.OK);
        }
        return response;
    }

    @PutMapping("/book")
    public ResponseEntity<List<BookDetails>> updateBook(@RequestBody AddBookRequest request) {
        ResponseEntity<List<BookDetails>> response = null;
        if (request.getIsbn() == null || !bookstoreService.checkIfBookIsInDB(request.getIsbn())) {
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            List<Book> books = bookstoreService.addBook(request);
            List<BookDetails> bookDetailsList = books.stream().map(book -> bookDetailsConverter.convert(book)).toList();
            response = new ResponseEntity<>(bookDetailsList, HttpStatus.OK);
        }
        return response;
    }

    @DeleteMapping("/book/{isbn}")
    public ResponseEntity<List<BookDetails>> deleteBook(@PathVariable(value = "isbn", required = true) String isbn) {
        ResponseEntity<List<BookDetails>> response = null;
        if (bookstoreService.checkIfBookIsInDB(isbn)) {
            List<Book> books = bookstoreService.deleteBook(isbn);
            List<BookDetails> bookDetailsList = books.stream().map(book -> bookDetailsConverter.convert(book)).toList();
            response = new ResponseEntity<>(bookDetailsList, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
