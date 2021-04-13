package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.service.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"product", "cart", "account"})
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    // 日志功能
    private final Logger logger = LoggerFactory.getLogger(CatalogController.class);

    @GetMapping("/catalog/main")
    public String viewMain(HttpSession session) {

        List<String> languages = new ArrayList<>();
        languages.add("English");
        languages.add("简体中文");
        session.setAttribute("languages", languages);

        logger.info("View main");
        return "catalog/main";
    }

    @GetMapping("/catalog/category")
    public String viewCategory(@RequestParam("categoryId") String categoryId, Model model) {

        if (categoryId != null) {
            Category category = catalogService.getCategory(categoryId);
            List<Product> productList = catalogService.getProductListByCategory(categoryId);
            model.addAttribute("category", category);
            model.addAttribute("productList", productList);
        }
        logger.info("View category, categoryId: " + categoryId);
        return "catalog/category";
    }

    @GetMapping("/catalog/product")
    public String viewProduct(@ModelAttribute("productId") String productId, HttpSession session, Model model) {
        if (productId != null) {
            Product product = catalogService.getProduct(productId);
            List<Item> itemList = catalogService.getItemListByProduct(productId);

            session.setAttribute("product", product);
            model.addAttribute("itemList", itemList);
            model.addAttribute("product", product);
        }
        logger.info("View product, productId: " + productId);
        return "catalog/product";
    }

    @GetMapping("/catalog/item")
    public String viewItem(@RequestParam("itemId") String itemId, Model model) {

        if (itemId != null) {
            Item item = catalogService.getItem(itemId);

            model.addAttribute("item", item);

        }
        logger.info("View Item, itemId: " + itemId);
        return "catalog/item";
    }

    @PostMapping("/searchProduct")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {

        if (keyword.trim().equals("")) {
            return "catalog/main";
        }

        List<Product> productList = catalogService.searchProductList(keyword);
        model.addAttribute("productList", productList);
        logger.info("Search product, keyword: " + keyword);
        return "catalog/searchProduct";
    }


}
