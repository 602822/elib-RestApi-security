/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;

/**
 * 
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

	@Autowired
	private AuthorService authorService;
	
	@GetMapping("/authors/{id}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<Author> getAuthor(@PathVariable("id") Long id) throws AuthorNotFoundException {
		
		Author author = authorService.findById(id);
		
		return new ResponseEntity<>(author, HttpStatus.OK);		
	}

	@GetMapping("/authors")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<Object> getAuthors() {
		List<Author> authors = authorService.findAll();
		if (authors.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(authors, HttpStatus.OK);
	}

	@PostMapping("/authors")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> createAuthor(@RequestBody Author author) {
		Author newAuthor = authorService.createAuthor(author);
		return new ResponseEntity<>(newAuthor, HttpStatus.CREATED);
	}

	@PutMapping("authors/{authorId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> updateAuthor(@PathVariable("authorId") int authorId, @RequestBody Author author) {
		try {
			authorService.updateAuthor(author,authorId);
			return new ResponseEntity<>(author, HttpStatus.OK);
		} catch (AuthorNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}


	@GetMapping("authors/{authorId}/books")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<Object> getAuthorBooks(@PathVariable("authorId") int authorId) throws AuthorNotFoundException {
		try {
			Set<Book> books =  authorService.getAuthorBooksOfAuthor(authorId);
			return new ResponseEntity<>(books, HttpStatus.OK);
		} catch (AuthorNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
		}

	}

}
