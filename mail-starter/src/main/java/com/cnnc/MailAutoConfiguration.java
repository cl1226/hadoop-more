package com.cnnc;

import com.cnnc.config.MailProperties;
import com.cnnc.service.MailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({MailProperties.class})
//@Import(MailService.class)
@ComponentScan
public class MailAutoConfiguration {
}
