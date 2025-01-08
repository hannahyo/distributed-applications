package be.ucll.da.postservice.domain.post;

public interface EventSender {

    void sendUserTaggedEvent(Post post);
}

