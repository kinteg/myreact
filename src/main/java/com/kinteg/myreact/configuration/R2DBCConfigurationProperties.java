package com.kinteg.myreact.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;

@ConfigurationProperties(prefix = "r2dbc")
@Data
public class R2DBCConfigurationProperties {

    @NotEmpty
    private String url;

    private String user;
    private String password;

}
