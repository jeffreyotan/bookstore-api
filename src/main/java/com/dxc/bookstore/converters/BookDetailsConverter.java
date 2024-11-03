package com.dxc.bookstore.converters;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import com.dxc.bookstore.entity.Author;
import com.dxc.bookstore.entity.Book;
import com.dxc.bookstore.models.BookDetails;
import com.dxc.bookstore.repository.AuthorRepository;

@Component
public class BookDetailsConverter {
    private AuthorRepository authorRepository;

    public BookDetailsConverter(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public BookDetails convert(Book book) {
        BookDetails newBookDetails = new BookDetails();
        if (book != null) {
            newBookDetails.setIsbn(book.getIsbn());
            newBookDetails.setTitle(book.getTitle());
            String authorListInString = book.getAuthors();
            if (authorListInString != null) {
                List<String> authorList = Arrays.asList(authorListInString.split(","));
                Iterable<Author> authors = authorRepository.findAll();
                List<Author> authorsFound = StreamSupport.stream(authors.spliterator(), false)
                    .filter(author -> authorList.stream()
                        .anyMatch(authorId -> authorId.equals(author.getId().toString())))
                    .toList();
                if (authorsFound.size() > 0) {
                    newBookDetails.setAuthors(authorsFound);
                }
            }
            newBookDetails.setYear(book.getPubyear());
            newBookDetails.setPrice(book.getPrice());
            newBookDetails.setGenre(book.getGenre());
        }
        return newBookDetails;
    }
}
