package it.ph.com.cn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品订单实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder implements Serializable {
    /**
     * 订单ID
     */
    private int id;

    /**
     * 用户ID
     */
    private int userId;

    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 商品名称
     */
    private String productName;

    /**
     * 订单流水号
     */
    private String tradeNo;

    /**
     * 价格
     */
    private int price;

    /**
     * 订单创建时间
     */
    private Date createTime;
}
