package br.ufsm.csi.spring.config;

import br.ufsm.csi.spring.security.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {
    @Bean
    public WebMvcConfigurer mvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new Interceptor())
                        .addPathPatterns("/**")
                        //paginas publicas
                        .excludePathPatterns(
                                "/",
                                "/login",
                                "/user/register-user",
                                "/css/**",
                                "/assets/**"
                        );
            }
        };
    }
}

