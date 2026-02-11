package filippotimo.Giorno_83.services;

import filippotimo.Giorno_83.Exceptions.NotFoundException;
import filippotimo.Giorno_83.entities.BlogPost;
import filippotimo.Giorno_83.payloads.NewBlogPostPayload;
import filippotimo.Giorno_83.repositories.BlogPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class BlogPostService {

    private final BlogPostsRepository blogPostsRepository;

    @Autowired
    public BlogPostService(BlogPostsRepository blogPostsRepository) {
        this.blogPostsRepository = blogPostsRepository;
    }

    // 1. GET -> Torna una lista di Blog Post

    public Page<BlogPost> findAllBlogPosts(int page, int size, String orderBy, String sortCriteria) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
        return this.blogPostsRepository.findAll(pageable);
    }

    // 2. GET -> Torna un singolo Blog Post specifico

    public BlogPost findBlogPostById(Long blogPostId) {
        return this.blogPostsRepository.findById(blogPostId).orElseThrow(() -> new NotFoundException(blogPostId));
    }

    // 3. POST -> Crea un Blog Post

    public BlogPost saveBlogPost(NewBlogPostPayload blogPostPayload) {

        BlogPost newBlogPost = new BlogPost(
                blogPostPayload.getCategoria(),
                blogPostPayload.getTitolo(),
                blogPostPayload.getContenuto(),
                blogPostPayload.getTempoDiLettura(),
                blogPostPayload.getAutohor()
        );
        Random rndm = new Random();
        int nRandom1 = rndm.nextInt(500);
        int nRandom2 = rndm.nextInt(500);
        newBlogPost.setCover("https://picsum.photos/" + nRandom1 + "/" + nRandom2);

        BlogPost savedBlogPost = this.blogPostsRepository.save(newBlogPost);

        System.out.println("Il blog post con id: " + savedBlogPost.getId() + " è stato aggiunto correttamente al DB!");

        return savedBlogPost;
    }

    // 4. PUT -> Modifica lo specifico Blog post

    public BlogPost findByIdAndUpdateBlogPost(Long blogPostId, NewBlogPostPayload blogPostPayload) {

        BlogPost found = this.findBlogPostById(blogPostId);

        found.setCategoria(blogPostPayload.getCategoria());
        found.setTitolo(blogPostPayload.getTitolo());
        found.setContenuto(blogPostPayload.getContenuto());
        found.setTempoDiLettura(blogPostPayload.getTempoDiLettura());

        BlogPost modifiedBlogPost = this.blogPostsRepository.save(found);

        System.out.println("Il blog post con id: " + modifiedBlogPost.getId() + " è stato modificato con successo");

        return modifiedBlogPost;
    }

    // 5. DELETE -> Cancella lo specifico Blog post

    public void findByIdAdDeleteBlogPost(Long blogPostId) {
        BlogPost found = this.findBlogPostById(blogPostId);
        this.blogPostsRepository.delete(found);
    }

}
