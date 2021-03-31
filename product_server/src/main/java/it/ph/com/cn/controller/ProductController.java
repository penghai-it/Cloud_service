package it.ph.com.cn.controller;

import it.ph.com.cn.model.ProductModel;
import it.ph.com.cn.service.impl.ProductServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {
    /**
     * 获取YML配置文件中的端口号,eureka地址，服务名称
     */
    @Value("${server.port}")
    private String port;

    @Value("${eureka.client.serviceUrl.defaultZone}")
    private String url;

    @Value("${spring.application.name}")
    private String userName;
    @Autowired
    private ProductServiceImpl productServiceImpl;

    /**
     * 查询所有商品详情
     *
     * @return
     */
    @RequestMapping("/istProduct")
    public List<ProductModel> istProduct() {
        List<ProductModel> productModels = productServiceImpl.listProduct();
        return productModels;
    }

    /**
     * 第一种传参方式
     *
     * @RequestMapping("/findById") public ProductModel findById(@RequestParam("id") int id){
     * localhost:9001/product/findById/1
     * }
     * 第二种传参方式
     * @RequestMapping("findById/{id}") public ProductModel findById(@PathVariable int id) {
     * http://localhost:9001/product/findById?id=1
     * }
     * <p>
     * }
     */


    /**
     * 根据商品ID查询商品详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public ProductModel findById(@RequestParam("id") int id) {
        ProductModel byId = productServiceImpl.findById(id);
        ProductModel result = new ProductModel();
        //新new一个对象将查询出来的值拷贝过去
        BeanUtils.copyProperties(byId, result);
        result.setName(result.getName() + ":date from port=" + port);
        System.out.println("服务名称:" + userName + "  " + "端口号:" + port);
        return result;
    }
}
