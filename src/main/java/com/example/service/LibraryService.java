package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.example.model.Author;
import com.example.model.Book;
import com.example.model.request.BookCreationRequest;
import com.example.repository.AuthorRepository;
import com.example.repository.BookRepository;
import com.example.repository.LendRepository;
import com.example.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibraryService {
	// 더이상 쓰지 못하게 final로 닫음
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;
	private final LendRepository lendRepository;
	private final MemberRepository memberRepository;
	
	public Book readBook(Long id) {
		Optional<Book> book=bookRepository.findById(id);
		if(book.isPresent()) {
			return book.get();
		}
		throw new EntityNotFoundException("cant find any book under given ID");
	}
	
	public List<Book> readBooks() {
		return bookRepository.findAll();
	}
	
	public Book readBook(String isbn) {
		Optional<Book> book = bookRepository.findByIsbn(isbn);
		if(book.isPresent()) {
			return book.get();
		}
		throw new EntityNotFoundException("cant find any book under given ISBN");
	}
	
	public Book createBook(BookCreationRequest book) {
		Optional<Author> author = authorRepository.findById(book.getAuthorId());
		if(!author.isPresent()) {
			throw new EntityNotFoundException("author not found");
		}
		Book bookToCreate = new Book();
		BeanUtils.copyProperties(book, bookToCreate);
		bookToCreate.setAuthor(author.get());
		return bookRepository.save(bookToCreate);
	}
}
