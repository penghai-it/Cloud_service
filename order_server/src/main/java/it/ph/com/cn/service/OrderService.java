package it.ph.com.cn.service;

import it.ph.com.cn.model.ProductOrder;

/**
 * 订单业务
 */
public interface OrderService {
    /**
     * 下单接口（使用Ribbon做负载均衡）
     *
     * @param userId
     * @param productId
     * @return
     */
    ProductOrder save(int userId, int productId);

    /**
     * 使用Feign作为负载均衡
     *
     * @param id
     * @return
     */
    ProductOrder findById(int id);
}
