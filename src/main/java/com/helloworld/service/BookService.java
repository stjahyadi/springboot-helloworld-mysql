package com.helloworld.service;

import com.helloworld.entity.Book;

import java.util.List;
import java.util.Map;

public interface BookService {
    public List<Book> findAll();

    public Book save(Book newBook);

    public Book saveOrUpdate(Book newBook, Long id);

    public Book findById(Long id);

    public void deleteById(Long id);

    public Book patch(Map<String, String> update, Long id);
}
