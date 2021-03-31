package it.ph.com.cn.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import it.ph.com.cn.model.ProductOrder;
import it.ph.com.cn.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderServiceImpl orderServiceImpl;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 使用Ribbon作为负载均衡
     *
     * @param userId
     * @param productId
     * @return
     */
    @RequestMapping("/save")
    //使用Hystrix做熔断降级处理（如果save方法出现异常就会调用saveOderFail方法）(API方式使用，也就是内部降级使用 )
    @HystrixCommand(fallbackMethod = "saveOderFail")
    public Object save(@RequestParam("userId") int userId, @RequestParam("productId") int productId) {
        try {
            TimeUnit.SECONDS.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        data.put("data", orderServiceImpl.save(userId, productId));
        return data;
    }

    //注意，方法签名和参数一定要和API方法一致
    public Object saveOderFail(int userId, int productId) {

        Map<String, Object> msg = new HashMap<>();
        msg.put("code", -1);
        msg.put("msg", "当前抢购人数过多，请稍候再试！");
        return msg;
    }


    /**
     * 试用Feign作为负载均衡
     * 如果请求的参数是一个对象那么就需要用@RequestBody注解，同时请求方式一定要是POST请求方式
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById/{id}")
    @HystrixCommand(fallbackMethod = "findByIdOderFail")
    public Object findById(@PathVariable int id) {
        /**
         * TimeUnit.SECONDS.sleep(10);等同于Thread.sleep(100);方法。不过建议使用前者
         * TimeUnit.SECONDS.sleep(10)；获取当前线程进行休眠(可读性更强)
         * SECONDS（秒）
         * MINUTES(分)
         */
    /*    try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        data.put("date", orderServiceImpl.findById(id));
        return data;
    }

    public Object findByIdOderFail(int id) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("code", -1);
        msg.put("msg", "服务器错误！没有查到商品，请稍候重试!");
        return msg;
    }
}
