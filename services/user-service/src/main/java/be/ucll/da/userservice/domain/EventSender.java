package be.ucll.da.userservice.domain;

public interface EventSender {
    void sendUserCreatedEvent(User user);
}
