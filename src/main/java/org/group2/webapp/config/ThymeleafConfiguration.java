package org.group2.webapp.config;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfiguration {

    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(ThymeleafConfiguration.class);

    @Bean
    @Description("Thymeleaf template resolver serving HTML 5 emails")
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding(CharEncoding.UTF_8);
        templateResolver.setOrder(1);
        return templateResolver;
    }
}
