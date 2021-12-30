package realworld.articles;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

@ApplicationScoped
public class ArticleRepository implements PanacheRepository<Article> {
    public Article findBySlug(String slug) {
        return find("slug", slug).singleResult();
    }

    public Stream<Article> findByTagName(String name) {
        return getEntityManager()
                .createQuery("SELECT a FROM Article a JOIN a.tags tag WHERE tag.name = :name", Article.class)
                .setParameter("name", name)
                .getResultStream();
    }

    public Stream<Article> findByAuthorUserName(String username) {
        return getEntityManager()
                .createQuery("SELECT a FROM Article a JOIN a.author author WHERE author.username = :username", Article.class)
                .setParameter("username", username)
                .getResultStream();
    }
}
