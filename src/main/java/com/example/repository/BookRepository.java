package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	Optional<Book> findByIsbn(String isbn);
}
