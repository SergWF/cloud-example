package my.wf.demo.cloud.author.dto;

import my.wf.demo.cloud.author.model.Author;
import org.immutables.value.Value.Immutable;

import java.util.UUID;

@Immutable
public interface AuthorExcerptListDto {
    UUID getId();
    String getName();
    String getLink();

    static AuthorExcerptListDto valueOf(Author author) {
        return null;
    }
}
