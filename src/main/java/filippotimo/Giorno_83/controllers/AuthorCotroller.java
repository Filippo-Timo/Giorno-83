package filippotimo.Giorno_83.controllers;

/*

1. GET /authors -> Ritorna la lista di autori (List<Author>)
2. GET /authors/123 -> Ritorna un singolo autore (Author)
3. POST /authors -> Crea un nuovo autore (new Author)
4. PUT /authors/123 -> Modifica lo specifico autore (Author)
5. DELETE /authors/123 -> Cancella lo specifico autore (void)

 */

import filippotimo.Giorno_83.entities.Author;
import filippotimo.Giorno_83.payloads.NewAuthorPayload;
import filippotimo.Giorno_83.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorCotroller {

    private final AuthorService authorService;

    @Autowired
    public AuthorCotroller(AuthorService authorService) {
        this.authorService = authorService;
    }

    // 1. GET /authors -> Ritorna la lista di autori (List<Author>)

    @GetMapping
    public Page<Author> findAllAuthors(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "3") int size,
                                       @RequestParam(defaultValue = "nome") String orderBy,
                                       @RequestParam(defaultValue = "asc") String sortCriteria) {
        return this.authorService.findAllAuthors(page, size, orderBy, sortCriteria);
    }

    // 2. GET /authors/123 -> Ritorna un singolo autore (Author)

    @GetMapping("/{authorId}")
    public Author findAuthorById(@PathVariable Long authorId) {
        return this.authorService.findAuthorById(authorId);
    }

    // 3. POST /authors -> Crea un nuovo autore (new Author)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Questo serve per ricevere un codice 201 se ha successo la creazione
    public Author createAuthor(@RequestBody NewAuthorPayload authorPayload) {
        return this.authorService.saveAuthor(authorPayload);
    }

    // 4. PUT /authors/123 -> Modifica lo specifico autore (Author)

    @PutMapping("/{authorId}")
    public Author findAuthorByIdAndUpdate(@PathVariable Long authorId, @RequestBody NewAuthorPayload authorPayload) {
        return this.authorService.findByIdAndUpdateAuthor(authorId, authorPayload);
    }

    // 5. DELETE /authors/123 -> Cancella lo specifico autore (void)

    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Questo serve per ricevere un codice 204 se ha successo la cancellazione
    public void findAuthorByIdAndDelete(@PathVariable Long authorId) {
        this.authorService.findByIdAdDeleteAuthor(authorId);
    }

}
