package com.riverlab.CrudApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.riverlab.CrudApp.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
