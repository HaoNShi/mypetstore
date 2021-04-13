package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@SessionAttributes({"account"})
public class AccountController {

    @Autowired
    private AccountService accountService;

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("/account/viewLoginForm")
    public String viewLoginForm() {
        return "account/login";
    }

    @PostMapping("/account/login")
    public String login(@Valid Account account, HttpSession session, Model model, BindingResult bindingResult) {
        logger.info("Login");
        if (bindingResult.hasErrors()) {
            return bindingResult.getFieldError().getDefaultMessage();
        }

        account = accountService.getAccount(account);

        if (account != null) {
            session.setAttribute("account", account);
            model.addAttribute("account", account);
            logger.info(account.getUsername() + " login success");
            return "catalog/main";
        } else {
            model.addAttribute("account", account);
            logger.info("Login Failure.");
            return "account/login";
        }

    }


    @GetMapping("/account/signOut")
    public String signOut(Model model, HttpSession session) {

        Account account = (Account) session.getAttribute("account");
        logger.info(account.getUsername() + " logout");

        account = null;

        session.setAttribute("account", account);
        model.addAttribute("account", account);
        return "catalog/main";
    }

    @GetMapping("/account/viewRegisterForm")
    public String viewRegisterForm(HttpSession session) {

        List<String> categories = new ArrayList<>();
        categories.add("FISH");
        categories.add("DOGS");
        categories.add("REPTILES");
        categories.add("CATS");
        categories.add("BIRDS");
        session.setAttribute("categories", categories);

        return "account/register";
    }

    @PostMapping("/account/register")
    public String register(Account account, Model model) {
        logger.info("register");
        if (!(account.getUsername().equals("") || account.getPassword().equals(""))) {
            if (account.getPassword().equals(account.getRepeatedPassword())) {
                Account temp = accountService.getAccount(account.getUsername());

                if (temp == null) {
                    accountService.insertAccount(account);
                    Account t = null;
                    model.addAttribute("account", t);
                    logger.info("Register Success");
                    return "account/login";
                }
            }

        }

        Account t = null;
        model.addAttribute("account", t);
        logger.info("Register failure");
        return "account/register";
    }

    @GetMapping("/account/editAccount")
    public String editAccount(HttpSession session, Model model) {
        logger.info("Edit Account");
        String message = null;
        List<String> languages = new ArrayList<>();
        languages.add("English");
        languages.add("简体中文");
        session.setAttribute("languages", languages);

        List<String> categories = new ArrayList<>();
        categories.add("FISH");
        categories.add("DOGS");
        categories.add("REPTILES");
        categories.add("CATS");
        categories.add("BIRDS");
        session.setAttribute("categories", categories);
        model.addAttribute("message", message);
        return "account/editAccount";
    }

    @PostMapping("/account/confirmEdit")
    public String confirmEdit(@Valid Account account, HttpSession session, Model model) {

        if (!account.getPassword().equals("") && !account.getRepeatedPassword().equals("") && account.getPassword().equals(account.getRepeatedPassword())) {
            accountService.updateAccount(account);
            session.setAttribute("account", account);
            model.addAttribute("account", account);

            return "catalog/main";
        } else {
            logger.info("Confirm Edit");
            return "account/editAccount";
        }

    }

    // AJAX 判断用户名是否存在
    @RequestMapping(value = "/account/usernameExists", method = {RequestMethod.POST})
    public void checkUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String name = request.getParameter("username");
        if (name.trim().equals("")) {
            out.print(2);// 不能为空
        } else {
            Account account = accountService.getAccount(name);
            if (account != null) {
                out.print(1);// 用户名已存在
            } else {
                out.print(3);// 用户名可以用
            }
        }
    }
}
