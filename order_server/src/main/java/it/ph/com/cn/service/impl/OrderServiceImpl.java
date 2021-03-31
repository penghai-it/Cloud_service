package it.ph.com.cn.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import it.ph.com.cn.model.ProductOrder;
import it.ph.com.cn.service.OrderClient;
import it.ph.com.cn.service.OrderService;
import it.ph.com.cn.utils.JsonUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {
    /**
     * 使用Ribbno做负载均衡所调用的接口
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 使用Feign做负载均衡所调用的接口
     */
    @Autowired
    private OrderClient orderClient;

    /**
     * 下单服务（使用Ribbon做负载均衡）
     *
     * @param userId
     * @param productId
     * @return
     */
    @Override
    public ProductOrder save(int userId, int productId) {
        //获取商品详情返回到一个Map集合
        Map<String, Object> productMap = restTemplate.getForObject("http://product-server/product/findById?id=" + productId, Map.class);
        ProductOrder productOrder = new ProductOrder();
        //下单时间
        productOrder.setCreateTime(new Date());
        //用户ID
        productOrder.setUserId(userId);
        //生成流水号
        productOrder.setTradeNo(UUID.randomUUID().toString());
        //从Map集合中取商品名称
        productOrder.setProductName(productMap.get("name").toString());
        //从Map集合中取商品价格  Integer.parseInt()方法是将String类型转化成int类型
        productOrder.setPrice(Integer.parseInt(productMap.get("price").toString()));
        return productOrder;

     /*   //第二种方式
        String forObject = restTemplate.getForObject("http://product-server/product/findById?id=" + productId, String.class);
        JsonNode jsonNode = JsonUtils.str2JsonNode(forObject);
        System.out.println(forObject);
        ProductOrder productOrder = new ProductOrder();
        //下单时间
        productOrder.setCreateTime(new Date());
        //用户ID
        productOrder.setUserId(userId);
        //生成流水号
        productOrder.setTradeNo(UUID.randomUUID().toString());
        //从Map集合中取商品名称
        productOrder.setProductName(jsonNode.get("name").toString());
        //从Map集合中取商品价格  Integer.parseInt()方法是将String类型转化成int类型
        productOrder.setPrice(Integer.parseInt(jsonNode.get("price").toString()));
        System.out.println(productOrder);
        return productOrder;*/
    }

    /**
     * 下单服务（使用Feign做负载均衡）
     *
     * @param id
     * @return
     */
    @Override
    public ProductOrder findById(int id) {
        //返回的是字符串对象就需要使用Json进行解析
        String response = orderClient.findById(id);
        JsonNode jsonNode = JsonUtils.str2JsonNode(response);
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProductName(jsonNode.get("name").toString());
        productOrder.setPrice(Integer.parseInt(jsonNode.get("price").toString()));
        productOrder.setCreateTime(new Date());
        productOrder.setTradeNo(UUID.randomUUID().toString());
        System.out.println(productOrder);
        return productOrder;
    }
}
