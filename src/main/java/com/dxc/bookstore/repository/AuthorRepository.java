package com.dxc.bookstore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dxc.bookstore.entity.Author;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    // no custom methods
}
