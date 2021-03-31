package it.ph.com.cn.service;

import it.ph.com.cn.model.ProductModel;

import java.util.List;

public interface ProductServuce {
    List<ProductModel> listProduct();

    ProductModel findById(int id);

}
