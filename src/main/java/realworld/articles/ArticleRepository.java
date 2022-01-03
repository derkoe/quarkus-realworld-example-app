package realworld.articles;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

import static io.quarkus.panache.common.Parameters.with;

import java.util.stream.Stream;

@ApplicationScoped
public class ArticleRepository implements PanacheRepository<Article> {
    public Article findBySlug(String slug) {
        return find("SELECT a FROM Article a LEFT JOIN a.favorites f WHERE a.slug = :slug", with("slug", slug)).singleResult();
    }

    public Stream<Article> findByTagName(String name) {
        return find("SELECT a FROM Article a LEFT JOIN a.favorites f LEFT JOIN a.tags tag WHERE tag.name = :name", with("name", name)).stream();
    }

    public Stream<Article> findByAuthorUserName(String username) {
        return find("SELECT a FROM Article a LEFT JOIN a.favorites f JOIN a.author author WHERE author.username = :username", with("username", username)).stream();
    }
}
