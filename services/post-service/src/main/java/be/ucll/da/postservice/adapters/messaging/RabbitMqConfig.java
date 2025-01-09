package be.ucll.da.postservice.adapters.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Jackson2JsonMessageConverter converter() {
        ObjectMapper mapper =
                new ObjectMapper()
                        .registerModule(new ParameterNamesModule())
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());

        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter, CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    @Bean
    public Declarables createValidateUserQueue(){
        return new Declarables(new Queue("q.user-service.validate-user"));
    }

    @Bean
    public Declarables createUserValidatedExchange(){
        return new Declarables(
                new FanoutExchange("x.user-validated"),
                new Queue("q.user-validated.post-service" ),
                new Binding("q.user-validated.post-service", Binding.DestinationType.QUEUE, "x.user-validated", "user-validated.post-service", null));
    }

    @Bean
    public Declarables createValidateTaggedUsers(){
        return new Declarables(new Queue("q.user-service.validate-tagged-users"));
    }

    @Bean
    public Declarables createTaggedUsersValidatedExchange(){
        return new Declarables(
                new FanoutExchange("x.tagged-users-validated"),
                new Queue("q.tagged-users-validated.post-service"),
                new Binding("q.tagged-users-validated.post-service", Binding.DestinationType.QUEUE, "x.tagged-users-validated", "tagged-users-validated.post-service", null));
    }

    @Bean
    public Declarables createSendEmailQueue(){
        return new Declarables(new Queue("q.notification-service.send-email"));
    }
}
