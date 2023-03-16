package com.example.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.model.Author;
import com.example.model.Book;
import com.example.model.Lend;
import com.example.model.LendStatus;
import com.example.model.Member;
import com.example.model.MemberStatus;
import com.example.model.request.AuthorCreationRequest;
import com.example.model.request.BookCreationRequest;
import com.example.model.request.BookLendRequest;
import com.example.model.request.MemberCreationRequest;
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
	
	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}
	
	public Member createMember(MemberCreationRequest request) {
		Member member = new Member();
		BeanUtils.copyProperties(request, member);
		return memberRepository.save(member);
	}
	
	public Member updateMember(Long id, MemberCreationRequest request) {
		Optional<Member> optionalMember = memberRepository.findById(id);
		if(!optionalMember.isPresent()) {
			throw new EntityNotFoundException("Member not present in the database");
		}
		Member member = optionalMember.get(); // id 값을 가지고 온다
		member.setLastName(request.getLastName());
		member.setFirstName(request.getFirstName());
		
		return memberRepository.save(member);
	}
	
	public Author createAuthor(AuthorCreationRequest request) {
		Author author = new Author();
		BeanUtils.copyProperties(request, author);
		return authorRepository.save(author);
	}
	
	public List<String> lendABook(BookLendRequest bookLendRequest) {
		List<String> booksApprovedToBurrow = new ArrayList<>();
		bookLendRequest.getBookIds().forEach(id->{
			Optional<Book> bookForId=bookRepository.findById(id);
			if(!bookForId.isPresent()) {
				throw new EntityNotFoundException("cant find any book under given ID");
			}
			
			Optional<Member> memberForId=
					memberRepository.findById(bookLendRequest.getMemberId());
			if(!memberForId.isPresent()) {
				throw new EntityNotFoundException("member not present in the database");
			}
			
			Member member = memberForId.get();
			if(member.getStatus() != MemberStatus.ACTIVE) {
				throw new RuntimeException("user is not active to proceed a lending");
			}
			
			Optional<Lend> burrowedBook = 
					lendRepository.findByBookAndStatus(bookForId.get(), LendStatus.BURROWED);
			if(!burrowedBook.isPresent()) {
				booksApprovedToBurrow.add(bookForId.get().getName());
				Lend lend = new Lend();
				lend.setMember(memberForId.get());
				lend.setStatus(LendStatus.BURROWED);
				lend.setStartOn(Instant.now());
				lend.setDueOn(Instant.now().plus(30,ChronoUnit.DAYS));
				lendRepository.save(lend);
			}
		});
		return booksApprovedToBurrow;
	}

}
