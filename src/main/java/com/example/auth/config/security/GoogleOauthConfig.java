package com.example.auth.config.security;

import com.example.auth.config.session.GoogleAuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import javax.servlet.Filter;

@Configuration
@EnableOAuth2Client
@AllArgsConstructor
public class GoogleOauthConfig {

    private final OAuth2ClientContext oAuth2ClientContext;
    private final GoogleAuthenticationSuccessHandler googleAuthenticationSuccessHandler;

    @Bean
    public Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter oauth2Filter = new OAuth2ClientAuthenticationProcessingFilter("/login");
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(googleClient(), oAuth2ClientContext);
        oauth2Filter.setRestTemplate(oAuth2RestTemplate);
        oauth2Filter.setTokenServices(new UserInfoTokenServices(googleResource().getUserInfoUri(), googleClient().getClientId()));
        oauth2Filter.setAuthenticationSuccessHandler(googleAuthenticationSuccessHandler);

        return oauth2Filter;
    }

    @Bean
    @ConfigurationProperties("google.client")
    public OAuth2ProtectedResourceDetails googleClient() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(-100);

        return registrationBean;
    }

//    @Bean
//    public static PropertySourcesPlaceholderConfigurer properties() {
//
//        System.out.println("google yml Call");
//
//        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//        yaml.setResources(new ClassPathResource("google-oauth.yml"));
//
//        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
//        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
//        return propertySourcesPlaceholderConfigurer;
//    }


}
