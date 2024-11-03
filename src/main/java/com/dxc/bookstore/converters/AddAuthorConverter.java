package com.dxc.bookstore.converters;

import org.springframework.stereotype.Component;

import com.dxc.bookstore.entity.Author;
import com.dxc.bookstore.models.AddAuthorRequest;

@Component
public class AddAuthorConverter {
    public Author convert(AddAuthorRequest request) {
        Author newAuthor = new Author();
        newAuthor.setName(request.getName());
        newAuthor.setBirthday(request.getBirthday());
        return newAuthor;
    }
}
