package my.wf.demo.cloud.author.controller;

import lombok.AllArgsConstructor;
import my.wf.demo.cloud.author.dto.AuthorDto;
import my.wf.demo.cloud.author.dto.AuthorExcerptListDto;
import my.wf.demo.cloud.author.service.AuthorsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api-author/v1/authors", produces = {"application/json;charset=UTF-8"})
public class AuthorsController {

    private final AuthorsService authorsService;


    @GetMapping(value = "/{id}")
    public AuthorDto getAuthor(@NotNull @PathVariable UUID id) {
        return authorsService.getAuthor(id, AuthorDto::valueOf);
    }

    @GetMapping
    public Page<AuthorExcerptListDto> getAuthors(@Nullable Pageable pageable){
        return authorsService.getPagedAuthors(AuthorExcerptListDto::valueOf, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto createAuthor(@Valid @RequestBody AuthorDto authorDto){
        return  authorsService.createAuthor(authorDto, AuthorDto::createAuthor, AuthorDto::valueOf);
    }

    @PutMapping(value = "/{id}")
    public AuthorDto updateAuthor(@PathVariable UUID id, @Valid @RequestBody AuthorDto authorDto){
        return  authorsService.updateAuthor(id, authorDto, AuthorDto::updateAuthor, AuthorDto::valueOf);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteAuthor(@PathVariable UUID id){
        authorsService.deleteAuthor(id);
    }

}
