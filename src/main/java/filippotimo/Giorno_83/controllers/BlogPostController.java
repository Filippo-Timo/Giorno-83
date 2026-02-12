package filippotimo.Giorno_83.controllers;

/*

1. GET /blogPosts -> Ritorna la lista di blog posts (List<BlogPost>)
2. GET /blogPosts/123 -> Ritorna un singolo blog post (BlogPost)
3. POST /blogPosts -> Crea un nuovo blog post (new BlogPost)
4. PUT /blogPosts/123 -> Modifica lo specifico blog post (BlogPost)
5. DELETE /blogPosts/123 -> Cancella lo specifico blog post (void)

*/

import filippotimo.Giorno_83.Exceptions.ValidationException;
import filippotimo.Giorno_83.entities.BlogPost;
import filippotimo.Giorno_83.payloads.NewBlogPostDTO;
import filippotimo.Giorno_83.payloads.UpdateBlogPostDTO;
import filippotimo.Giorno_83.services.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogPosts")
public class BlogPostController {

    private final BlogPostService blogPostService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    // 1. GET /blogPosts -> Ritorna la lista di autori (List<BlogPost>)

    @GetMapping
    public Page<BlogPost> findAllBlogPosts(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "titolo") String orderBy,
                                           @RequestParam(defaultValue = "asc") String sortCriteria) {
        return this.blogPostService.findAllBlogPosts(page, size, orderBy, sortCriteria);
    }

    // 2. GET /blogPosts/123 -> Ritorna un singolo autore (BlogPost)

    @GetMapping("/{blogPostId}")
    public BlogPost findBlogPostById(@PathVariable Long blogPostId) {
        return this.blogPostService.findBlogPostById(blogPostId);
    }

    // 3. POST /blogPosts -> Crea un nuovo autore (new BlogPost)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Questo serve per ricevere un codice 201 se ha successo la creazione
    public BlogPost createBlogPost(@RequestBody @Validated NewBlogPostDTO newBlogPostDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.blogPostService.saveBlogPost(newBlogPostDTO);
        }
    }

    // 4. PUT /blogPosts/123 -> Modifica lo specifico blog post (BlogPost)

    @PutMapping("/{blogPostId}")
    public BlogPost findBlogPostByIdAndUpdate(@PathVariable Long blogPostId, @RequestBody @Validated UpdateBlogPostDTO updateBlogPostDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.blogPostService.findByIdAndUpdateBlogPost(blogPostId, updateBlogPostDTO);
        }
    }

    // 5. DELETE /blogPosts/123 -> Cancella lo specifico blog post (void)

    @DeleteMapping("/{blogPostId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Questo serve per ricevere un codice 204 se ha successo la cancellazione
    public void findBlogPostByIdAndDelete(@PathVariable Long blogPostId) {
        this.blogPostService.findByIdAdDeleteBlogPost(blogPostId);
    }


}
