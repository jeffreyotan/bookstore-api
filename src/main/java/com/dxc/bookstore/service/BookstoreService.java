package com.dxc.bookstore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.dxc.bookstore.converters.AddAuthorConverter;
import com.dxc.bookstore.entity.Author;
import com.dxc.bookstore.entity.Book;
import com.dxc.bookstore.models.AddAuthorRequest;
import com.dxc.bookstore.models.AddBookRequest;
import com.dxc.bookstore.repository.AuthorRepository;
import com.dxc.bookstore.repository.BookstoreRepository;

@Service
public class BookstoreService {
    private AuthorRepository authorRepository;
    private BookstoreRepository bookstoreRepository;
    private AddAuthorConverter addAuthorConverter;

    public BookstoreService(AuthorRepository authorRepository,
                            BookstoreRepository bookstoreRepository,
                            AddAuthorConverter addAuthorConverter) {
        this.authorRepository = authorRepository;
        this.bookstoreRepository = bookstoreRepository;
        this.addAuthorConverter = addAuthorConverter;
    }

    public List<Author> getAuthor(String name) {
        Iterable<Author> authors = authorRepository.findAll();
        return StreamSupport.stream(authors.spliterator(), false)
            .filter(author -> name.equals(author.getName()))
            .toList();
    }

    public List<Author> getAuthors() {
        Iterable<Author> authors = authorRepository.findAll();
        return StreamSupport.stream(authors.spliterator(), false).toList();
    }

    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    public Author addAuthor(AddAuthorRequest request) {
        Author newAuthor = null;
        Iterable<Author> authorsInDB = authorRepository.findAll();
        List<Author> authorsThatMatch = StreamSupport.stream(authorsInDB.spliterator(), false)
                                            .filter(author -> checkAnyMatchingAuthor(request, author))
                                            .toList();
        if (authorsThatMatch.size() == 0) {
            newAuthor = this.authorRepository.save(addAuthorConverter.convert(request));
        }
        return newAuthor;
    }

    public List<Book> getAllBooks() {
        Iterable<Book> books = bookstoreRepository.findAll();
        return StreamSupport.stream(books.spliterator(), false).toList();
    }

    public List<Book> getBooksByTitle(String title) {
        Iterable<Book> books = bookstoreRepository.findAll();
        return StreamSupport.stream(books.spliterator(), false)
            .filter(book -> title.equals(book.getTitle()))
            .toList();
    }

    public List<Book> getBooksByAuthor(String author) {
        List<Book> booksFound = new ArrayList<>();
        List<Author> authors = getAuthor(author);
        if (authors.size() > 0) {
            Iterable<Book> books = bookstoreRepository.findAll();
            booksFound = StreamSupport.stream(books.spliterator(), false)
                .filter(book -> authors.stream()
                    .anyMatch(newAuthor -> book.getAuthors().contains(newAuthor.getId().toString())))
                .toList();
        }
        return booksFound;
    }

    public List<Book> getBooksByAuthorAndTitle(String title, String author) {
        List<Book> booksFound = new ArrayList<>();
        List<Author> authors = getAuthor(author);
        if (authors.size() > 0) {
            Iterable<Book> books = bookstoreRepository.findAll();
            booksFound = StreamSupport.stream(books.spliterator(), false)
                .filter(book -> authors.stream()
                    .anyMatch(newAuthor -> book.getAuthors().contains(newAuthor.getId().toString())))
                .filter(book -> title.equals(book.getTitle()))
                .toList();
        }
        return booksFound;
    }

    public Optional<Book> getBookByIsbn(String isbn) {
        return bookstoreRepository.findById(isbn);
    }

    public boolean checkIfBookIsInDB(String isbn) {
        boolean isBookInDB = false;
        Optional<Book> bookFoundInDB = getBookByIsbn(isbn);
        if (bookFoundInDB.isPresent()) {
            isBookInDB = true;
        }
        return isBookInDB;
    }

    public List<Book> addBook(AddBookRequest request) {
        List<Book> newBookList = new ArrayList<>();
        
        Book newBook = new Book();
        newBook.setIsbn(request.getIsbn());
        newBook.setTitle(request.getTitle());
        newBook.setPubyear(request.getYear());
        newBook.setPrice(request.getPrice());
        newBook.setGenre(request.getGenre());
        List<String> authorIds = new ArrayList<>();
        for (AddAuthorRequest newAuthor: request.getAuthors()) {
            Author authorAdded = addAuthor(newAuthor);
            if (authorAdded != null) {
                authorIds.add(authorAdded.getId().toString());
            } else {
                List<Author> authors = getAuthor(newAuthor.getName());
                authors.forEach(currentAuthor -> {
                    if (currentAuthor.getBirthday() != null &&
                        currentAuthor.getBirthday().equals(newAuthor.getBirthday())) {
                        authorIds.add(currentAuthor.getId().toString());
                    } else if (newAuthor.getBirthday() == null) {
                        authorIds.add(currentAuthor.getId().toString());
                    }
                });
            }
        }
        if (authorIds.size() > 0) {
            newBook.setAuthors(String.join(",", authorIds));
        } else {
            newBook.setAuthors("");
        }
        bookstoreRepository.save(newBook);
        newBookList.add(newBook);

        return newBookList;
    }

    public List<Book> deleteBook(String isbn) {
        List<Book> books = new ArrayList<>();
        Optional<Book> book = getBookByIsbn(isbn);
        if (book.isPresent()) {
            books.add(book.get());
            bookstoreRepository.deleteById(isbn);
        }
        return books;
    }

    private boolean checkAnyMatchingAuthor(AddAuthorRequest request, Author author) {
        boolean isFound = false;
        if (request.getName() != null) {
            if (request.getBirthday() != null) {
                isFound = request.getName().equals(author.getName())
                    && request.getBirthday().equals(author.getBirthday());
            } else {
                isFound = request.getName().equals(author.getName());
            }
        } else {
            if (request.getBirthday() != null) {
                isFound = request.getBirthday().equals(author.getBirthday());
            }
        }
        return isFound;
    }
}
