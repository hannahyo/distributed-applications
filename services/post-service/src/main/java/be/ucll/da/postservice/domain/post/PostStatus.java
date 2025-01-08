package be.ucll.da.postservice.domain.post;

public enum PostStatus {
    VALIDATING_USER,
    NO_USER,
    VALIDATING_TAGGED_USERS,
    POST_CREATED,
    TAGGED_USERS_INVALID
}
