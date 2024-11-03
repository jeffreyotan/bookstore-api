package com.dxc.bookstore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dxc.bookstore.entity.Book;

@Repository
public interface BookstoreRepository extends CrudRepository<Book, String> {
    // no custom methods
}
