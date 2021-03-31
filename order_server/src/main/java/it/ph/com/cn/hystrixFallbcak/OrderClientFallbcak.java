package it.ph.com.cn.hystrixFallbcak;

import it.ph.com.cn.service.OrderClient;
import org.springframework.stereotype.Component;

/**
 * 针对商品服务，服务降级
 */
@Component
public class OrderClientFallbcak implements OrderClient {
    @Override
    public String findById(int id) {
        System.out.println("商品查询服务出错了，进行了服务降级");
        return null;
    }
}
