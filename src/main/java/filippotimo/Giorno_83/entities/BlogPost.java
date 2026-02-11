package filippotimo.Giorno_83.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blogposts")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    private String categoria;
    private String titolo;
    private String cover;
    private String contenuto;
    private int tempoDiLettura;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    public BlogPost(String categoria, String titolo, String contenuto, int tempoDiLettura, Author author) {
        this.categoria = categoria;
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.tempoDiLettura = tempoDiLettura;
        this.author = author;
    }
}
