package com.jd.common.bean.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnExpression("!'${ignore}'.isEmpty()")
@ConfigurationProperties(prefix = "ignore")
public class FilterIgnorePropertiesConfig {
	private List<String> urls = new ArrayList<>();

	private List<String> excludes = new ArrayList<>();

	private List<String> clients = new ArrayList<>();
}
