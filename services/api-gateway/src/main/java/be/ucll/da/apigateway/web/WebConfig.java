package be.ucll.da.apigateway.web;

import be.ucll.da.apigateway.client.user.api.UserApi;
import be.ucll.da.apigateway.client.feed.api.FeedApi;
import be.ucll.da.apigateway.client.post.api.PostApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public UserApi userApi() {
        return new UserApi();
    }

    @Bean
    public FeedApi feedApi() {
        return new FeedApi();
    }

    @Bean
    public PostApi postApi() {
        return new PostApi();
    }
}
