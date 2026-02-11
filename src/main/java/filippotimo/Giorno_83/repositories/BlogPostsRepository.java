package filippotimo.Giorno_83.repositories;

import filippotimo.Giorno_83.entities.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostsRepository extends JpaRepository<BlogPost, Long> {
}
