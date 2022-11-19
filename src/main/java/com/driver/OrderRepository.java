package com.driver;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Component
public class OrderRepository {
    HashMap<String , Order> listOfOrder ;
    HashMap<String,DeliveryPartner> listOfPartner;
    HashMap<String, List<Order>> listOfPair;

    public OrderRepository() {
        listOfOrder = new HashMap<>();
        listOfPartner = new HashMap<>();
        listOfPair = new HashMap<>();
    }
    //Add an Order: POST /orders/add-order Pass the Order object as request body
    // Return success message wrapped in a ResponseEntity object Controller Name - addOrder
    public void addOrder(Order order){
        listOfOrder.put(order.getId(),order);
    }

    //  Add a Delivery Partner: POST /orders/add-partner/{partnerId} Pass the partnerId string as path variable
    //  Return success message wrapped in a ResponseEntity object Controller Name - addPartner
    public void addPartner(String partnername){
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnername);
        listOfPartner.put(partnername , deliveryPartner);
    }
    //Assign an order to a partner: PUT /orders/add-order-partner-pair  Pass orderId and partnerId strings as request parameters 
    //Return success message wrapped in a ResponseEntity object  Controller Name - addOrderPartnerPair

    public void addPair(String orderId , String partnerId ){
        Order order = listOfOrder.get(orderId);
        List<Order> list = new ArrayList<>();
        if(listOfPair.containsKey(partnerId)){
            list = listOfPair.get(partnerId);
        }
        list.add(order);
        listOfPair.put(partnerId , list);

    }
    //Get Order by orderId: GET /orders/get-order-by-id/{orderId}
    // Pass orderId string as path variable
    // Return Order object wrapped in a ResponseEntity object Controller Name - getOrderById

    public Order getOrderById(String orderId){
        return listOfOrder.get(orderId);
    }

   // Get Partner by partnerId: GET /orders/get-partner-by-id/{partnerId}  
    // Pass partnerId string as path variable  
    // Return DeliveryPartner object wrapped in a ResponseEntity object  Controller Name - getPartnerById
    public DeliveryPartner getDeliveryById(String partnerId){
        return listOfPartner.get(partnerId);
    }

    //Get number of orders assigned to given partnerId: GET /orders/get-order-count-by-partner-id/{partnerId}
    //  Pass partnerId as path variable  
    // Return Integer wrapped in a ResponseEntity object 
    // Controller Name - getOrderCountByPartnerId

    public int getCountOrder(String partnerId){
        return listOfPair.get(partnerId).size();
    }

    //Get List of all orders assigned to given partnerId: GET /orders/get-orders-by-partner-id/{partnerId} 
    // Pass partnerId as path variable 
    // Return List of Order object wrapped in a ResponseEntity object  
    // Controller Name - getOrdersByPartnerId

    public List<String> getAllOrderByPartner(String partnerId){
        List<Order> list = listOfPair.get(partnerId);
        List<String> ans = new ArrayList<>();
        for(Order o : list){
            ans.add(o.getId());
        }
        return ans;
    }

    //Get List of all orders in the system: GET /orders/get-all-orders
    // No params or body required Return List of Order object wrapped in a ResponseEntity object
    // Controller Name - getAllOrders
    public List<String> getAllOrder(){
        List<String> list = new ArrayList<>();
        for (Order o : listOfOrder.values()){
            list.add(o.getId());
        }
        return list;
    }
    //Get count of orders which are not assigned to any partner: GET /orders/get-count-of-unassigned-orders
    // No params or body required
    // Return Integer wrapped in a ResponseEntity object
    // Controller Name - getCountOfUnassignedOrders
    public int getOrderCount(){
        int fullOrderSize = listOfOrder.size();
        int assignOrderSize = 0;
        for(List<Order> list : listOfPair.values()){
            assignOrderSize = assignOrderSize + list.size();
        }
        int ans = fullOrderSize - assignOrderSize ;
        return ans;
    }
    //Get count of orders which are left undelivered by partnerId after given time
    // : GET /orders/get-count-of-orders-left-after-given-time
    // Pass time string (in HH:MM format) and partnerId string as path variable
    // Return Integer wrapped in a ResponseEntity object
    // Controller Name - getOrdersLeftAfterGivenTimeByPartnerId

    public int getRemainCount(String time , String partnerId){
        String s[] = time.split(":");
        int ans = Integer.parseInt(s[0])*60 + Integer.parseInt(s[1]);
        int count = 0;
        List<Order> orders = listOfPair.get(partnerId);
        for(Order o : orders){
            if(o.getDeliveryTime() > ans){
                count++;
            }
        }
        return count;
    }
    //Get the time at which the last delivery is made by given partner: GET /orders/get-last-delivery-time/{partnerId}
    // Pass partnerId string as path variable
    // Return String with format HH:MM wrapped in a ResponseEntity object  
    // Controller Name - getLastDeliveryTimeByPartnerId
    public String getLastDeliveryTime(String partnerId){
        int time = listOfPair.get(partnerId).get(listOfPair.get(partnerId).size() - 1).getDeliveryTime();
        int hour = time / 60;
        int min = time % 60;
        String h = "";
        String m = "";
        if(hour < 10){
            h += "0" + hour;
        }
        if(min < 10){
            m += "0"+ min;
        }
        String ans = h +":"+m;
        return ans;
    }

    //Delete a partner and the corresponding orders should be unassigned: DELETE /orders/delete-partner-by-id/{partnerId}
    // Pass partnerId as path variable
    // Return success message wrapped in a ResponseEntity object
    // Controller Name - deletePartnerById
    public void deleteByPartner(String partnerId){
        listOfPartner.remove(partnerId);
        listOfPair.remove(partnerId);
    }

    //Delete an order and the corresponding partner should be unassigned: DELETE /orders/delete-order-by-id/{orderId}
    // Pass orderId as path variable Return success message wrapped in a ResponseEntity object
    // Controller Name - deleteOrderById
    // remove it from the assigned order of that partnerId
    public void deleteByOrder(String orderId){
        listOfOrder.remove(orderId);
        for(String s  : listOfPair.keySet()){
            List<Order> list = listOfPair.get(s);
            for(Order o : list){
                if(o.getId().equals(orderId)){
                    list.remove(o);
                    listOfPair.put(s , list);
                    break;
                }
            }
        }
    }
}
