package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.CartItem;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.service.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

@Controller
public class CartController {

    private final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/cart/cart")
    public String addItemToCart(@ModelAttribute("workingItemId") String workingItemId, HttpSession session, Model model) {

        if (workingItemId != null) {
            Cart cart = (Cart) session.getAttribute("cart");

            if (cart == null || cart.getNumberOfItems() == 0) {
                cart = new Cart();
            }

            if (cart.containsItemId(workingItemId)) {
                cart.incrementQuantityByItemId(workingItemId);
            } else {
                boolean isInStock = catalogService.isItemInStock(workingItemId);
                Item item = catalogService.getItem(workingItemId);
                cart.addItem(item, isInStock);
                logger.info("Add item, itemId: " + item.getItemId());
            }

            session.setAttribute("cart", cart);
            model.addAttribute("cart", cart);
        }

        return "cart/cart";
    }

    @GetMapping("/cart/viewCart")
    public String viewCart(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = new Cart();
        }

        session.setAttribute("cart", cart);
        model.addAttribute("cart", cart);

        return "cart/cart";
    }

    @GetMapping("/cart/removeItemFromCart")
    public String removeItemFromCart(@RequestParam("workingItemId") String workingItemId, HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");

        Item item = cart.removeItemById(workingItemId);
        logger.info("Remove item, itemId: " + item.getItemId());
        if (item == null) {
            model.addAttribute("message", "Attempted to remove null CartItem from Cart.");
            return "error";
        } else {
            return "cart/cart";
        }
    }

    // AJAX 购物车刷新
    @RequestMapping("/cart/updateCartQuantity")
    public void updateCartQuantity(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) return;
        Iterator<CartItem> cartItems = cart.getAllCartItems();
        String eleId = request.getParameter("eleId");

        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        while (cartItems.hasNext()) {
            CartItem cartItem = cartItems.next();
            String itemId = cartItem.getItem().getItemId();
            if (eleId.equals(itemId)) {
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                cart.setQuantityByItemId(itemId, quantity);
                if (quantity < 1) {
                    cartItems.remove();
                    out.println("<msg>NotExist" + "#" + cart.getSubTotal() + "</msg>");
                } else {
                    out.println("<msg>$" + cartItem.getTotal() + "#$" + cart.getSubTotal() + "</msg>");
                }
                break;
            }
        }
        cart.getSubTotal();
        session.setAttribute("cart", cart);
        out.flush();
        out.close();
        logger.info("Update cart");
        return;
    }
}
