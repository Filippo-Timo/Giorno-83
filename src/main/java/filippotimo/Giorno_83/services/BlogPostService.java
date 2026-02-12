package filippotimo.Giorno_83.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import filippotimo.Giorno_83.Exceptions.NotEmptyException;
import filippotimo.Giorno_83.Exceptions.NotFoundException;
import filippotimo.Giorno_83.entities.Author;
import filippotimo.Giorno_83.entities.BlogPost;
import filippotimo.Giorno_83.payloads.NewBlogPostDTO;
import filippotimo.Giorno_83.payloads.UpdateBlogPostDTO;
import filippotimo.Giorno_83.repositories.AuthorsRepository;
import filippotimo.Giorno_83.repositories.BlogPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

@Service
public class BlogPostService {

    private final BlogPostsRepository blogPostsRepository;
    private final AuthorsRepository authorsRepository;
    private final Cloudinary cloudinaryUploader;

    @Autowired
    public BlogPostService(BlogPostsRepository blogPostsRepository, AuthorsRepository authorsRepository, Cloudinary cloudinaryUploader) {
        this.blogPostsRepository = blogPostsRepository;
        this.authorsRepository = authorsRepository;
        this.cloudinaryUploader = cloudinaryUploader;
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

    public BlogPost saveBlogPost(NewBlogPostDTO newBlogPostDTO) {

        Author author = authorsRepository.findById(newBlogPostDTO.autohorId()).orElseThrow(() -> new NotFoundException(newBlogPostDTO.autohorId()));

        BlogPost newBlogPost = new BlogPost(
                newBlogPostDTO.categoria(),
                newBlogPostDTO.titolo(),
                newBlogPostDTO.contenuto(),
                newBlogPostDTO.tempoDiLettura(),
                author
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

    public BlogPost findByIdAndUpdateBlogPost(Long blogPostId, UpdateBlogPostDTO updateBlogPostDTO) {

        BlogPost found = this.findBlogPostById(blogPostId);

        found.setCategoria(updateBlogPostDTO.categoria());
        found.setTitolo(updateBlogPostDTO.titolo());
        found.setContenuto(updateBlogPostDTO.contenuto());
        found.setTempoDiLettura(updateBlogPostDTO.tempoDiLettura());

        BlogPost modifiedBlogPost = this.blogPostsRepository.save(found);

        System.out.println("Il blog post con id: " + modifiedBlogPost.getId() + " è stato modificato con successo");

        return modifiedBlogPost;
    }

    // 5. DELETE -> Cancella lo specifico Blog post

    public void findByIdAdDeleteBlogPost(Long blogPostId) {
        BlogPost found = this.findBlogPostById(blogPostId);
        this.blogPostsRepository.delete(found);
    }

    // 6. PATCH -> Modifica la cover del BlogPost in particolare

    public BlogPost findByIdAndUploadCover(Long blogPostId, MultipartFile file) {
        // 1. Controlli (es. dimensione non può superare tot, oppure tipologia file solo .gif...)
        if (file.isEmpty()) throw new NotEmptyException();
        // 2. Find by id del BlogPost...
        BlogPost found = this.findBlogPostById(blogPostId);
        // 3. Upload del file su Cloudinary
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl = (String) result.get("secure_url");
            // 4. Cloudinary ci torna l'url dell'immagine che salviamo dentro il BlogPost trovato
            // ...aggiorno l'utente cambiandogli l'url dell'avatar
            found.setCover(imageUrl);
            // 5. Return del BlogPost
            return found;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
