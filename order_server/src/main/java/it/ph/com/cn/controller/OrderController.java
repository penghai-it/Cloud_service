package it.ph.com.cn.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import it.ph.com.cn.service.impl.OrderServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderServiceImpl orderServiceImpl;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 使用Ribbon作为负载均衡
     * HttpServletRequest 可以从里面获取IP地址
     *
     * @param userId
     * @param productId
     * @param request
     * @return
     */
    @RequestMapping("/save")
    //使用Hystrix做熔断降级处理（如果save方法出现异常就会调用saveOderFail方法）(API方式使用，也就是内部降级使用 )
    @HystrixCommand(fallbackMethod = "saveOderFail")
    public Object save(@RequestParam("userId") int userId, @RequestParam("productId") int productId, HttpServletRequest request) {
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
    public Object saveOderFail(int userId, int productId, HttpServletRequest request) {
        String saveOrderKey = "save-order";
        //获取IP地址
        String remoteAddr = request.getRemoteAddr();
        String sendValue = stringRedisTemplate.opsForValue().get(saveOrderKey);
        //重新开一个线程来执行发短信的业务，这样就能实现异步，否则是同步实现的会很耗时
        new Thread(() -> {
            //判断是不是为空
            if (StringUtils.isBlank(sendValue)) {
                System.out.println("紧急短信，用户下单失败，请迅速查找原因并处理,IP地址是:" + remoteAddr);
                //这里调用短信服务（目前应为没有）TODO

                //将错误信息保存到Redis中，并设置20秒的存活时间
                stringRedisTemplate.opsForValue().set(saveOrderKey, "下单失败", 20, TimeUnit.SECONDS);
            } else {
                System.out.println("已经发送过短信,20秒内不重复发!");
            }
        }).start();


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
