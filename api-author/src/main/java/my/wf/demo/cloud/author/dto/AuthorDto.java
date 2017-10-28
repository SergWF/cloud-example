package my.wf.demo.cloud.author.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import my.wf.demo.cloud.author.model.Author;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.immutables.value.Value.Immutable;


@Immutable
@JsonDeserialize(as = ImmutableAuthorDto.class)
@JsonInclude(NON_NULL)
public interface AuthorDto {
    @NotNull
    @Size(min = 1, max = 255)
    String getName();
    @NotNull
    @Size(min = 1, max = 2048)
    String getLink();

    static AuthorDto valueOf(Author author) {
        return ImmutableAuthorDto.builder()
                                 .name(author.getName())
                                 .link(author.getLink())
                                 .build();
    }

    static Author createAuthor(AuthorDto dto) {
        return Author.builder()
                     .name(dto.getName())
                     .link(dto.getLink())
                     .build();
    }

    static Author updateAuthor(AuthorDto dto, Author author) {
        return author.toBuilder()
                     .name(dto.getName())
                     .link(dto.getLink())
                     .build();
    }
}
