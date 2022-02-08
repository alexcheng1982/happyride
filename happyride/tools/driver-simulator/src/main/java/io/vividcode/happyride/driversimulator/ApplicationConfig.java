package io.vividcode.happyride.driversimulator;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.UrlBasedViewResolver;

@Configuration
@EnableWebFlux
public class ApplicationConfig implements WebFluxConfigurer {

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("*.html", "/*.js", "/*.css")
        .addResourceLocations("classpath:/public/");
  }

  @Override
  public void configureViewResolvers(final ViewResolverRegistry registry) {
    registry.viewResolver(new UrlBasedViewResolver());
  }
}
