package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Book;
import com.example.model.Lend;
import com.example.model.LendStatus;

public interface LendRepository extends JpaRepository<Lend, Long> {
	Optional<Lend> findByBookAndStatus(Book book, LendStatus status);
}
