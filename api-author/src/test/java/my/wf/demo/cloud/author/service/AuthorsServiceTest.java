package my.wf.demo.cloud.author.service;

import my.wf.demo.cloud.author.dto.AuthorDto;
import my.wf.demo.cloud.author.dto.ImmutableAuthorDto;
import my.wf.demo.cloud.author.error.AuthorNotFoundException;
import my.wf.demo.cloud.author.model.Author;
import my.wf.demo.cloud.author.repository.AuthorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthorsServiceTest {

    private static final UUID AUTHOR_ID = UUID.randomUUID();
    private static final String AUTHOR_NAME = "some name";
    private static final String AUTHOR_LINK = "http://some_link";
    private static final Pageable PAGE = new PageRequest(0, Integer.MAX_VALUE);

    @Mock
    private AuthorRepository authorRepository;
    private static final Author AUTHOR = Author.builder()
                                               .id(AUTHOR_ID)
                                               .name(AUTHOR_NAME)
                                               .link(AUTHOR_LINK)
                                               .build();
    private static final AuthorDto AUTHOR_DTO = ImmutableAuthorDto.builder()
                                                                  .name(AUTHOR_NAME)
                                                                  .link(AUTHOR_LINK)
                                                                  .build();

    @InjectMocks
    private AuthorsService authorsService;

    @Test
    public void shouldReturnAuthorById() throws Exception {
        //given
        doReturn(Optional.of(AUTHOR)).when(authorRepository).findById(AUTHOR_ID);

        //when
        AuthorDto responseDto = authorsService.getAuthor(AUTHOR_ID, AuthorDto::valueOf);

        // then
        assertThat(responseDto).isEqualTo(AUTHOR_DTO);
    }

    @Test
    public void shouldRaiseAuthorNotFound() throws Exception {
        //given
        doReturn(Optional.empty()).when(authorRepository).findById(AUTHOR_ID);
        //when //then
        assertThatThrownBy(() -> authorsService.getAuthor(AUTHOR_ID, AuthorDto::valueOf)).isInstanceOf(
                AuthorNotFoundException.class).hasMessage("Author not found by id " + AUTHOR_ID);
    }

    @Test
    public void shouldReturnPagedAuthors() throws Exception {
        //given
        List<Author> authors = Arrays.asList(
                Author.builder().name(AUTHOR_NAME + "_1").link(AUTHOR_LINK + "_1").build(),
                Author.builder().name(AUTHOR_NAME + "_2").link(AUTHOR_LINK + "_2").build(),
                Author.builder().name(AUTHOR_NAME + "_3").link(AUTHOR_LINK + "_3").build()
        );
        Page<Author> page = new PageImpl<>(authors);
        doReturn(page).when(authorRepository).findAll(PAGE);

        //when //then
        assertThat(authorsService.getPagedAuthors(AuthorDto::valueOf, PAGE).getContent())
                .containsExactlyInAnyOrder(
                        ImmutableAuthorDto.builder().name(AUTHOR_NAME + "_1").link(AUTHOR_LINK + "_1").build(),
                        ImmutableAuthorDto.builder().name(AUTHOR_NAME + "_2").link(AUTHOR_LINK + "_2").build(),
                        ImmutableAuthorDto.builder().name(AUTHOR_NAME + "_3").link(AUTHOR_LINK + "_3").build()
                );
    }

    @Test
    public void shouldCreateAuthor() throws Exception {
        //given
        Author notsaved = Author.builder().name(AUTHOR_NAME).link(AUTHOR_LINK).build();
        doReturn(AUTHOR).when(authorRepository).save(notsaved);
        //when
        AuthorDto responseDto = authorsService.createAuthor(AUTHOR_DTO, AuthorDto::createAuthor, AuthorDto::valueOf);
        // then
        assertThat(responseDto).isEqualTo(ImmutableAuthorDto.builder()
                                                            .name(AUTHOR_NAME)
                                                            .link(AUTHOR_LINK)
                                                            .build());
    }

    @Test
    public void shouldRaiseAuthorNotFoundOnUpdate() throws Exception {
        doReturn(Optional.empty()).when(authorRepository).findById(AUTHOR_ID);
        assertThatThrownBy(() -> authorsService.updateAuthor(AUTHOR_ID, AUTHOR_DTO, AuthorDto::updateAuthor, AuthorDto::valueOf)).isInstanceOf(
                AuthorNotFoundException.class).hasMessage("Author not found by id " + AUTHOR_ID);
    }

    @Test
    public void shouldUpdateAuthor() throws Exception {
        //given
        Author updated = Author.builder().id(AUTHOR_ID).name(AUTHOR_NAME).link(AUTHOR_LINK).build();

        doReturn(Optional.of(AUTHOR)).when(authorRepository).findById(AUTHOR_ID);

        doReturn(AUTHOR).when(authorRepository).save(updated);

        //when
        AuthorDto responseDto =
                authorsService.updateAuthor(AUTHOR_ID, AUTHOR_DTO, AuthorDto::updateAuthor, AuthorDto::valueOf);
        // then
        assertThat(responseDto).isEqualTo(ImmutableAuthorDto.builder()
                                                            .name(AUTHOR_NAME)
                                                            .link(AUTHOR_LINK)
                                                            .build());
    }

    @Test
    public void shouldDeleteAuthor() throws Exception {
        //given

        doReturn(Optional.of(AUTHOR)).when(authorRepository).findById(AUTHOR_ID);

        //when
        authorsService.deleteAuthor(AUTHOR_ID);
        // then

        verify(authorRepository).delete(AUTHOR);
    }

    @Test
    public void shouldRaiseAuthorNotFoundOnDelete() throws Exception {
        doReturn(Optional.empty()).when(authorRepository).findById(AUTHOR_ID);
        assertThatThrownBy(() -> authorsService.deleteAuthor(AUTHOR_ID)).isInstanceOf(
                AuthorNotFoundException.class).hasMessage("Author not found by id " + AUTHOR_ID);
    }

}