package it.ph.com.cn.service;

import com.netflix.client.AbstractLoadBalancerAwareClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import it.ph.com.cn.hystrixFallbcak.OrderClientFallbcak;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.GET;
import java.util.Map;

//使用Feign做负载均衡调用商品服务

/**
 * fallback = OrderClientFallbcak.class 自定义的一个服务类，用于服务降级
 * 注意：这里使用的是Feign中的Hystrix进行熔断降级，所以需要手动的在yml中开启
 */
@FeignClient(name = "product-server", fallback = OrderClientFallbcak.class)
public interface OrderClient {
    /**
     * !!!这里如果用@RequestMapping（）最好要指定请求方式。
     * 如果请求的参数是一个对象那么就需要用@RequestBody注解，同时请求方式一定要是POST请求方式
     */

    @RequestMapping(value = "product/findById", method = RequestMethod.GET)
    String findById(@RequestParam("id") int id);
}
