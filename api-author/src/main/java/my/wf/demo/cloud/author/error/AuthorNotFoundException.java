package my.wf.demo.cloud.author.error;


import java.util.UUID;

public class AuthorNotFoundException extends RuntimeException {
    private final UUID authorId;

    public AuthorNotFoundException(UUID authorId) {
        this.authorId = authorId;
    }

    @Override
    public String getMessage() {
        return String.format("Author not found by id %s", authorId);
    }
}
