package it.ph.com.cn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 使用Ribbon做负载均衡
 */
@SpringBootApplication
@EnableEurekaClient
/**
 * 使用Feign作为负载均衡
 */
@EnableFeignClients
/**
 * hystrix做熔断服务降级
 */
@EnableCircuitBreaker
/**
 * 这里也可以使用@SpringCloudApplicatio来代替部分注解
 */
class OrderServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServerApplication.class, args);
    }

    /**
     * 使用ribbon作为负债均衡
     *
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
