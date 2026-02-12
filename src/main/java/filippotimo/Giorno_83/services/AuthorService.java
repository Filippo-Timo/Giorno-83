package filippotimo.Giorno_83.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import filippotimo.Giorno_83.Exceptions.BadRequestException;
import filippotimo.Giorno_83.Exceptions.NotEmptyException;
import filippotimo.Giorno_83.Exceptions.NotFoundException;
import filippotimo.Giorno_83.entities.Author;
import filippotimo.Giorno_83.payloads.NewAuthorDTO;
import filippotimo.Giorno_83.repositories.AuthorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class AuthorService {

    private final AuthorsRepository authorsRepository;
    private final Cloudinary cloudinaryUploader;

    @Autowired
    public AuthorService(AuthorsRepository authorsRepository, Cloudinary cloudinaryUploader) {
        this.authorsRepository = authorsRepository;
        this.cloudinaryUploader = cloudinaryUploader;
    }

    // 1. GET -> Torna una pagina di un numero definito di Authors

    public Page<Author> findAllAuthors(int page, int size, String orderBy, String sortCriteria) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
        return this.authorsRepository.findAll(pageable);
    }

    // 2. GET -> Torna un singolo Author specifico

    public Author findAuthorById(Long authorId) {
        return this.authorsRepository.findById(authorId).orElseThrow(() -> new NotFoundException(authorId));
    }

    // 3. POST -> Crea un Author

    public Author saveAuthor(NewAuthorDTO authorDTO) {

        this.authorsRepository.findByEmail(authorDTO.email()).ifPresent(author -> {
            throw new BadRequestException("L'email " + author.getEmail() + " è già in uso!");
        });

        Author newAuthor = new Author(
                authorDTO.nome(),
                authorDTO.cognome(),
                authorDTO.email(),
                authorDTO.dataDiNascita()
        );
        newAuthor.setAvatar("https://ui-avatars.com/api?name=" + authorDTO.nome() + "+" + authorDTO.cognome());

        Author savedAuthor = this.authorsRepository.save(newAuthor);

        System.out.println("L'autore " + savedAuthor.getNome() + " " + savedAuthor.getCognome() + " è stato aggiunto correttamente al DB!");

        return savedAuthor;
    }

    // 4. PUT -> Modifica lo specifico Blog post

    public Author findByIdAndUpdateAuthor(Long authorId, NewAuthorDTO authorDTO) {

        Author found = this.findAuthorById(authorId);

        if (!found.getEmail().equals(authorDTO.email()))
            this.authorsRepository.findByEmail(authorDTO.email()).ifPresent(author -> {
                throw new BadRequestException("L'email " + author.getEmail() + " è già in uso!");
            });

        found.setNome(authorDTO.nome());
        found.setCognome(authorDTO.cognome());
        found.setEmail(authorDTO.email());
        found.setEmail(authorDTO.email());
        found.setAvatar("https://ui-avatars.com/api?name=" + authorDTO.nome() + "+" + authorDTO.cognome());

        Author modifiedAuthor = this.authorsRepository.save(found);

        System.out.println("L'utente con id: " + modifiedAuthor.getId() + " è stato modificato con successo");

        return modifiedAuthor;
    }

    // 5. DELETE -> Cancella lo specifico Blog post

    public void findByIdAdDeleteAuthor(Long authorId) {
        Author found = this.findAuthorById(authorId);
        this.authorsRepository.delete(found);
    }

    // 6. PATCH -> Modifica l'avatar dell'autore in particolare

    public Author findByIdAndUploadAvatar(Long authorId, MultipartFile file) {
        // 1. Controlli (es. dimensione non può superare tot, oppure tipologia file solo .gif...)
        if (file.isEmpty()) throw new NotEmptyException();
        // 2. Find by id dell'utente...
        Author found = this.findAuthorById(authorId);
        // 3. Upload del file su Cloudinary
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl = (String) result.get("secure_url");
            // 4. Cloudinary ci torna l'url dell'immagine che salviamo dentro l'autore trovato
            // ...aggiorno l'utente cambiandogli l'url dell'avatar
            found.setAvatar(imageUrl);
            // 5. Return dell'url
            return authorsRepository.save(found);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
