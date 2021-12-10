package com.helloworld.service.impl;

import com.helloworld.entity.Book;
import com.helloworld.exception.BookNotFoundException;
import com.helloworld.exception.BookUnSupportedFieldPatchException;
import com.helloworld.repository.BookRepository;
import com.helloworld.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository repository;

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public Book save(Book newBook) {
        return repository.save(newBook);
    }

    @Override
    public Book saveOrUpdate(Book newBook, Long id) {
        return repository.findById(id).map(x -> {
            x.setName(newBook.getName());
            x.setAuthor(newBook.getAuthor());
            x.setPrice(newBook.getPrice());
            return save(x);
        }).orElseGet(() -> {
            newBook.setId(id);
            return save(newBook);
        });
    }

    @Override
    public Book findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Book patch(Map<String, String> update, Long id) {
        return repository.findById(id).map(x -> {
            String author = update.get("author");
            if (!StringUtils.isEmpty(author)) {
                x.setAuthor(author);
                // better create a custom method to update a value = :newValue where id = :id
                return save(x);
            } else {
                throw new BookUnSupportedFieldPatchException(update.keySet());
            }
        }).orElseGet(() -> {
            throw new BookNotFoundException(id);
        });
    }
}
