package com.lephuduy.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lephuduy.laptopshop.domain.Cart;
import com.lephuduy.laptopshop.domain.CartDetail;
import com.lephuduy.laptopshop.domain.Order;
import com.lephuduy.laptopshop.domain.OrderDetail;
import com.lephuduy.laptopshop.domain.Product;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.repository.CartDetailRepository;
import com.lephuduy.laptopshop.repository.CartRepository;
import com.lephuduy.laptopshop.repository.OrderDetailRepository;
import com.lephuduy.laptopshop.repository.OrderRepository;
import com.lephuduy.laptopshop.repository.ProductRepository;
import com.lephuduy.laptopshop.repository.RoleRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ProductService(ProductRepository productRepository, CartDetailRepository cartDetailRepository,
            CartRepository cartRepository, UserService userService,
            OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public Product handleSaveProduct(Product product) {
        Product duy = this.productRepository.save(product);
        System.out.println(duy);
        return duy;
    }

    public List<Product> fecthProducts() {
        return this.productRepository.findAll();
    }

    public Product getProductById(long id) {
        return this.productRepository.findById(id);
    }

    public void deleteAProduct(long id) {
        this.productRepository.deleteById(id);
    }

    public void handleAddProductToCard(String email, long productId, HttpSession session) {
        // check user đã có cart chưa
        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            Cart cart = this.cartRepository.findByUser(user);

            if (cart == null) {
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setSum(0);

                cart = this.cartRepository.save(otherCart);
            }

            // save details cart
            Product product = this.productRepository.findById(productId);
            boolean isExistsProductInCart = this.cartDetailRepository.existsByCartAndProduct(cart, product);

            CartDetail oldCartDetail = this.cartDetailRepository.findByCartAndProduct(cart, product);
            if (oldCartDetail == null) {
                CartDetail cartDetail = new CartDetail();
                cartDetail.setCart(cart);
                cartDetail.setProduct(product);
                cartDetail.setPrice(product.getPrice());
                cartDetail.setQuantity(1);

                this.cartDetailRepository.save(cartDetail);

                // update sum order
                int newSum = cart.getSum() + 1;
                cart.setSum(newSum);
                this.cartRepository.save(cart);
                session.setAttribute("sum", newSum);

            } else {
                oldCartDetail.setQuantity(oldCartDetail.getQuantity() + 1);
                this.cartDetailRepository.save(oldCartDetail);
            }

        }
        // save cart
    }

    public Cart findByUser(User user) {
        // TODO Auto-generated method stub
        return this.cartRepository.findByUser(user);
    }

    public void deleteProductFromCart(long cartDetailId, HttpSession session) {
        Optional<CartDetail> cartDetailOptional = this.cartDetailRepository.findById(cartDetailId);

        if (cartDetailOptional.isPresent()) {
            CartDetail cartDetail = cartDetailOptional.get();

            Cart currentCart = cartDetail.getCart();

            this.cartDetailRepository.deleteById(cartDetailId);

            if (currentCart.getSum() == 1) {
                this.cartRepository.delete(currentCart);
                session.setAttribute("sum", 0);

            } else {
                int sum = currentCart.getSum();
                currentCart.setSum(sum - 1);
                session.setAttribute("sum", sum - 1);
                this.cartRepository.save(currentCart);
            }
        }

    }

    public void handleUpdateCartBeforeCheckOut(List<CartDetail> cartDetails) {
        for (CartDetail cartDetail : cartDetails) {
            Optional<CartDetail> optionalCartDetail = this.cartDetailRepository.findById(cartDetail.getId());
            if (optionalCartDetail.isPresent()) {
                CartDetail currenCartDetail = optionalCartDetail.get();
                currenCartDetail.setQuantity(cartDetail.getQuantity());
                this.cartDetailRepository.save(currenCartDetail);
            }
        }
    }

    public void handlePlaceOrder(User user, String receiverName, String receiverAddress, String receiverPhone) {
        // tạo mới order và orderDetail
        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setReceiverAddress(receiverAddress);
        order.setReceiverPhone(receiverPhone);
        order = this.orderRepository.save(order);

        Cart cart = this.cartRepository.findByUser(user);
        if (cart != null) {
            List<CartDetail> cartDetails = cart.getCartDetails();
            if (cartDetails != null) {
                for (CartDetail cartDetail : cartDetails) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setQuantity(cartDetail.getQuantity());
                    orderDetail.setProduct(cartDetail.getProduct());
                    orderDetail.setPrice(cartDetail.getPrice());

                    this.orderDetailRepository.save(orderDetail);
                }

                // delete cart and cartdetail
                for (CartDetail cartDetail : cartDetails) {
                    this.cartDetailRepository.deleteById(cartDetail.getId());
                    ;
                }

                this.cartRepository.deleteById(cart.getId());
            }
        }

    }
}
