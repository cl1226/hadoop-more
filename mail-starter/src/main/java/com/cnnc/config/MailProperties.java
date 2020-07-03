package com.cnnc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "common.mail")
public class MailProperties {

    private String msg;

}
