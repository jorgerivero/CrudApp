package com.riverlab.CrudApp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.riverlab.CrudApp.model.Book;
import com.riverlab.CrudApp.repository.BookRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BookController {

	@Autowired
	BookRepository bookRepository;
	
	@GetMapping("/getAllBooks")
	public ResponseEntity<List<Book>> getAllBooks() {
		
		try {
			List<Book> bookList = new ArrayList<>();
			bookRepository.findAll().forEach(bookList::add);
			
			if (bookList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(bookList, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	
	@GetMapping("/getBookById/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable Long id) {
		Optional<Book> oBook = bookRepository.findById(id);
		
		if (oBook.isPresent()) {
			return new ResponseEntity<>(oBook.get(), HttpStatus.OK);
		}
		
		return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/addBook")
	public ResponseEntity<Book> addBook(@RequestBody Book book) {
		Book newBook = bookRepository.save(book);
		
		return new ResponseEntity<Book>(newBook, HttpStatus.OK);
	}
	
	@PutMapping("/updateBookById/{id}")
	public ResponseEntity<Book> updateBookById(@PathVariable Long id, @RequestBody Book newBook) {
		
		try {
			Optional<Book> oBook = bookRepository.findById(id);
			
			if (oBook.isPresent()) {
				Book updatedBook = (Book)oBook.get();
				updatedBook.setTitle(newBook.getTitle());
				updatedBook.setAuthor(newBook.getAuthor());
				bookRepository.save(updatedBook);
				return new ResponseEntity<Book>(updatedBook, HttpStatus.OK);
			}
			
			return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
			
		} catch (Exception e) {
			return new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<HttpStatus> deleteBookById(@PathVariable Long id) {
		try {
			Optional<Book> oBook = bookRepository.findById(id);
			
			log.info("delete-----> oBook --> {}", oBook);
			
			if (oBook.isPresent()) {
				log.info("delete-----> oBook {}", oBook.get().getTitle());
				log.debug("delete RESPONSE: {}", (Book)oBook.get());
				bookRepository.delete(oBook.get());
				return new ResponseEntity<>(HttpStatus.OK);
			}
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
//		try {
//			bookRepository.deleteById(id);
//			return new ResponseEntity<>(HttpStatus.OK);
//			
//		} catch (Exception e) {
//			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
	}
	
}
