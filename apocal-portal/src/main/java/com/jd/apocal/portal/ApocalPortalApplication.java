package com.jd.apocal.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.jd.apocal.portal", "com.jd.common.bean"})
@EnableTransactionManagement
@EnableDiscoveryClient
public class ApocalPortalApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApocalPortalApplication.class, args);
  }

}
