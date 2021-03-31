package it.ph.com.cn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel implements Serializable {
    private Integer id;

    private String name;

    private int price;

    private int store;
}
