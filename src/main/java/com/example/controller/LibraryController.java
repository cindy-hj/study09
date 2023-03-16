package com.example.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Author;
import com.example.model.Book;
import com.example.model.Member;
import com.example.model.request.AuthorCreationRequest;
import com.example.model.request.BookCreationRequest;
import com.example.model.request.BookLendRequest;
import com.example.model.request.MemberCreationRequest;
import com.example.service.LibraryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value="/api/library")
@RequiredArgsConstructor
public class LibraryController {
	
	private final LibraryService libraryService;
	
	@GetMapping("/book")
	public ResponseEntity readBooks(@RequestParam(required=false)String isbn) {
		if(isbn == null) {
			return ResponseEntity.ok(libraryService.readBooks());
		}
		return ResponseEntity.ok(libraryService.readBook(isbn));
	}
	
	@GetMapping("/book/{bookId}")
	public ResponseEntity<Book> readBooks(@PathVariable Long bookId) {
		return ResponseEntity.ok(libraryService.readBook(bookId));
	}
	
	@PostMapping("/book")
	public ResponseEntity<Book> createBook(@RequestBody BookCreationRequest book) {
		return ResponseEntity.ok(libraryService.createBook(book));
	}
	
	@DeleteMapping("/book/{bookId}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/member")
	public ResponseEntity<Member> createMember(@RequestBody MemberCreationRequest member) {
		return ResponseEntity.ok(libraryService.createMember(member));
	}
	
	@PostMapping("/member/{memberId}")
	public ResponseEntity<Member> updateMember(@RequestBody Long memberId, MemberCreationRequest member) {
		return ResponseEntity.ok(libraryService.updateMember(memberId, member));
	}
	
	@PostMapping("/book/lend")
	public ResponseEntity<List<String>> lendABook(@RequestBody BookLendRequest bookLendRequests) {
		return ResponseEntity.ok(libraryService.lendABook(bookLendRequests));
	}
	
	@PostMapping("/author")
	public ResponseEntity<Author> createAuthor(@RequestBody AuthorCreationRequest request) {
		return ResponseEntity.ok(libraryService.createAuthor(request));
	}
}
