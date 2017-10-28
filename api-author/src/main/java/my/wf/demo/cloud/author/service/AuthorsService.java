package my.wf.demo.cloud.author.service;

import lombok.AllArgsConstructor;
import my.wf.demo.cloud.author.error.AuthorNotFoundException;
import my.wf.demo.cloud.author.model.Author;
import my.wf.demo.cloud.author.repository.AuthorRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class AuthorsService {

    private final AuthorRepository authorRepository;

    public <T> T getAuthor(UUID id, Function<Author, T> mapper) {
        return authorRepository.findById(id)
                               .map(mapper)
                               .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public <T> T createAuthor(T data, Function<T, Author> mapperIn, Function<Author, T> mapperOut) {
        final Author author = mapperIn.apply(data);
        final Author saved = authorRepository.save(author);
        return mapperOut.apply(saved);
    }

    public <T> Page<T> getPagedAuthors(Converter<Author, T> mapper, Pageable pageable) {
        return authorRepository.findAll(pageable).map(mapper);
    }

    public <T> T updateAuthor(UUID id, T data, BiFunction<T, Author, Author> updater, Function<Author, T> mapperOut) {
        final Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        Author updated = updater.apply(data, author);
        final Author saved = authorRepository.save(updated);
        return mapperOut.apply(saved);
    }

    public void deleteAuthor(UUID id) {
        final Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        authorRepository.delete(author);
    }
}
