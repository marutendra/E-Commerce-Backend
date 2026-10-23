package com.Learning.Order.Service;

import com.Learning.Order.DTO.*;
import com.Learning.Order.Entity.Order_Entity;
import com.Learning.Order.Entity.Order_Item;
import com.Learning.Order.Entity.Order_Status;
import com.Learning.Order.Feign.Seller_Client;
import com.Learning.Order.Feign.User_Client;
import com.Learning.Order.Repository.Order_Repository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class Order_Service {

    @Autowired
    Order_Repository orderRepository;
    @Autowired
    Seller_Client sellerClient;
    @Autowired
    User_Client userClient;

    // =====================================================================
    //  PLACE ORDER
    //  1. read the user's cart (User service)
    //  2. for every cart item: get current price, then reduce stock (Seller service)
    //  3. save the order
    //  4. clear the cart (User service)
    //  If anything fails in step 2 or 3, stock already reduced is given back.
    // =====================================================================
    public Order_Entity placeOrder(Place_Order_Request request) {
        if (request.getUserId() == null || request.getUserId().isBlank()) {
            throw new IllegalArgumentException("userId is required");
        }
        if (request.getShippingAddress() == null || request.getShippingAddress().isBlank()) {
            throw new IllegalArgumentException("Shipping address is required");
        }

        Cart_Response cart = getCart(request.getUserId());
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Your cart is empty");
        }

        List<Order_Item> orderItems = new ArrayList<>();
        List<Stock_Update_Request> reducedSoFar = new ArrayList<>();
        double total = 0;
        String currentProductId = null;
        Order_Entity savedOrder;

        try {
            for (Cart_Item_Response cartItem : cart.getItems()) {
                currentProductId = cartItem.getProductid();

                // use the CURRENT price from the seller, not the price saved in the cart
                Product_Response product = sellerClient.getProduct(currentProductId);

                Stock_Update_Request stock =
                        new Stock_Update_Request(currentProductId, cartItem.getQuantity());
                sellerClient.reduceStock(stock);      // fails if not enough stock
                reducedSoFar.add(stock);

                Order_Item item = new Order_Item();
                item.setProductId(currentProductId);
                item.setProductName(product.getProductName());
                item.setPrice(product.getPrice());
                item.setQuantity(cartItem.getQuantity());
                item.setItemTotal(product.getPrice() * cartItem.getQuantity());
                orderItems.add(item);

                total += item.getItemTotal();
            }

            Order_Entity order = new Order_Entity();
            order.setUserId(request.getUserId());
            order.setItems(orderItems);
            order.setTotalAmount(total);
            order.setShippingAddress(request.getShippingAddress());
            order.setStatus(Order_Status.PLACED);
            order.setOrderDate(LocalDateTime.now());
            savedOrder = orderRepository.save(order);

        } catch (RuntimeException e) {
            rollbackStock(reducedSoFar);          // give back stock already reduced
            throw translate(e, currentProductId);
        }

        // Order is safely saved. If clearing the cart fails we only log it;
        // the customer must not lose a valid order because of that.
        try {
            userClient.clearCart(request.getUserId());
        } catch (Exception e) {
            log.warn("Order {} placed but cart of user {} could not be cleared: {}",
                    savedOrder.getOrderId(), request.getUserId(), e.getMessage());
        }

        return savedOrder;
    }

    // =====================================================================
    //  READ
    // =====================================================================
    public Order_Entity getOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));
    }

    public List<Order_Entity> getOrdersByUser(String userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    // =====================================================================
    //  CANCEL  (only while the order is still PLACED). Gives stock back.
    // =====================================================================
    public Order_Entity cancelOrder(String orderId) {
        Order_Entity order = getOrder(orderId);

        if (order.getStatus() != Order_Status.PLACED) {
            throw new IllegalStateException(
                    "Only PLACED orders can be cancelled. Current status: " + order.getStatus());
        }

        for (Order_Item item : order.getItems()) {
            sellerClient.restoreStock(new Stock_Update_Request(item.getProductId(), item.getQuantity()));
        }

        order.setStatus(Order_Status.CANCELLED);
        return orderRepository.save(order);
    }

    // =====================================================================
    //  UPDATE STATUS  (PLACED -> SHIPPED -> DELIVERED). Cancel has its own endpoint.
    // =====================================================================
    public Order_Entity updateStatus(String orderId, Order_Status newStatus) {
        Order_Entity order = getOrder(orderId);

        if (newStatus == Order_Status.CANCELLED) {
            throw new IllegalArgumentException("Use the cancel endpoint to cancel an order");
        }
        if (order.getStatus() == Order_Status.CANCELLED || order.getStatus() == Order_Status.DELIVERED) {
            throw new IllegalStateException("Order is already " + order.getStatus());
        }
        if (newStatus.ordinal() <= order.getStatus().ordinal()) {
            throw new IllegalStateException(
                    "Cannot move order from " + order.getStatus() + " to " + newStatus);
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    // =====================================================================
    //  helpers
    // =====================================================================
    private Cart_Response getCart(String userId) {
        try {
            return userClient.getCart(userId);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Your cart is empty");
        }
    }

    private void rollbackStock(List<Stock_Update_Request> reduced) {
        for (Stock_Update_Request stock : reduced) {
            try {
                sellerClient.restoreStock(stock);
            } catch (Exception ex) {
                // worst case: stock stays reduced. Log it so it can be fixed by hand.
                log.error("Could not restore stock for product {} (quantity {}): {}",
                        stock.getProductId(), stock.getQuantity(), ex.getMessage());
            }
        }
    }

    // turns raw Feign errors into messages a customer can understand
    private RuntimeException translate(RuntimeException e, String productId) {
        if (e instanceof FeignException.NotFound) {
            return new NoSuchElementException("A product in your cart no longer exists: " + productId);
        }
        if (e instanceof FeignException.BadRequest) {
            return new IllegalArgumentException("Not enough stock for product: " + productId);
        }
        return e;
    }
}
