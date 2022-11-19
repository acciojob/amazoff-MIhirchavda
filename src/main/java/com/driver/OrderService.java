package com.driver;


import org.mapstruct.AfterMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void addO(Order order){
        orderRepository.addOrder(order);
    }
    public  void addP(String name){
        orderRepository.addPartner(name);
    }
    public  void addPa(String o , String  p){
        orderRepository.addPair(o , p);
    }
    public Order getO(String orderId){
       return orderRepository.getOrderById(orderId);
    }
    public DeliveryPartner getP(String partnerId){
        return orderRepository.getDeliveryById(partnerId);
    }
    public int getCount(String partnerId){
        return orderRepository.getCountOrder(partnerId);
    }
    public List<String> getAllOrderByP(String partnerId){
        return orderRepository.getAllOrderByPartner(partnerId);
    }
    public List<String> getAllO(){
        return orderRepository.getAllOrder();
    }
    public int getOrderC(){
        return orderRepository.getOrderCount();
    }
    public int remainC(String time , String partnerId){
        return orderRepository.getRemainCount(time ,partnerId);
    }
    public String lastOrderTime(String partnerId){
        return orderRepository.getLastDeliveryTime(partnerId);
    }
    public void deleteByP(String partnerId){
        orderRepository.deleteByPartner(partnerId);
    }
    public void deleteByO(String orderId){
        orderRepository.deleteByOrder(orderId);
    }




}
