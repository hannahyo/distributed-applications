package be.ucll.da.postservice.domain.post;

public enum PostStatus {
    VALIDATING_TAGGED_USERS,
    TAGGED_USERS_INVALID,
    POST_CREATED,
    PENDING,
    APPROVED,
    REJECTED
}
