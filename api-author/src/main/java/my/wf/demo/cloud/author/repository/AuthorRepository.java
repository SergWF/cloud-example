package my.wf.demo.cloud.author.repository;

import my.wf.demo.cloud.author.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends Repository<Author, UUID>{
    Optional<Author> findById(UUID id);
    Author save(Author author);
    Page<Author> findAll(Pageable pageable);
    void delete(Author author);
}
