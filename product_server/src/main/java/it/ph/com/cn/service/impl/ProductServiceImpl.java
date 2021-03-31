package it.ph.com.cn.service.impl;


import it.ph.com.cn.model.ProductModel;
import it.ph.com.cn.service.ProductServuce;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductServuce {
    public static final Map<Integer, ProductModel> daoMap = new HashMap<>();

    static {
        ProductModel p1 = new ProductModel(01, "电视", 3999, 20);
        ProductModel p2 = new ProductModel(02, "烤箱", 999, 10);
        ProductModel p3 = new ProductModel(03, "洗衣机", 1299, 50);
        ProductModel p4 = new ProductModel(04, "电脑", 10000, 15);
        ProductModel p5 = new ProductModel(05, "iphonex", 8999, 500);
        ProductModel p6 = new ProductModel(06, "冰箱", 5999, 3);
        daoMap.put(p1.getId(), p1);
        daoMap.put(p2.getId(), p2);
        daoMap.put(p3.getId(), p3);
        daoMap.put(p4.getId(), p4);
        daoMap.put(p5.getId(), p5);
        daoMap.put(p6.getId(), p6);
    }

    @Override
    public List<ProductModel> listProduct() {
        Collection<ProductModel> collection = daoMap.values();
        List<ProductModel> list = new ArrayList<>(collection);
        return list;
    }

    @Override
    public ProductModel findById(int id) {
        return daoMap.get(id);
    }


}
