package com.jd.apocal.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.jd.apocal.auth", "com.jd.common.bean"})
@EnableTransactionManagement
@EnableDiscoveryClient
public class ApocalAuthApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApocalAuthApplication.class, args);
  }

}
