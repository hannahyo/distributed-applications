package be.ucll.da.userservice.adapters.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Declarables createUserCreatedSchema(){
        return new Declarables(
                new FanoutExchange("x.user-created"),
                new Queue("q.user-feed-service" ),
                QueueBuilder.durable("q.user-notification-service")
                        .withArgument("x-dead-letter-exchange","x.notification-failure")
                        .withArgument("x-dead-letter-routing-key","fall-back")
                        .build(),
                new Binding("q.user-feed-service", Binding.DestinationType.QUEUE, "x.user-created", "user-feed-service", null),
                new Binding("q.user-notification-service", Binding.DestinationType.QUEUE, "x.user-created", "user-notification-service", null));
    }

    @Bean
    public Declarables createDeadLetterSchema(){
        return new Declarables(
                new DirectExchange("x.notification-failure"),
                new Queue("q.fall-back-notification"),
                new Binding("q.fall-back-notification", Binding.DestinationType.QUEUE,"x.notification-failure", "fall-back", null)
        );
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter, CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }
}
