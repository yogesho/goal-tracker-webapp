package com.goaltracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        ServletWebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> attrs = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(
                ErrorAttributeOptions.Include.MESSAGE,
                ErrorAttributeOptions.Include.EXCEPTION,
                ErrorAttributeOptions.Include.BINDING_ERRORS));

        Object statusObj = attrs.getOrDefault("status", 500);
        int status = (statusObj instanceof Number) ? ((Number) statusObj).intValue() : 500;
        String error = String.valueOf(attrs.getOrDefault("error", "Unexpected Error"));
        String message = String.valueOf(attrs.getOrDefault("message", "Something went wrong"));
        String path = String.valueOf(attrs.getOrDefault("path", request.getRequestURI()));

        model.addAttribute("status", status);
        model.addAttribute("error", error);
        model.addAttribute("message", message);
        model.addAttribute("path", path);
        model.addAttribute("is404", status == HttpStatus.NOT_FOUND.value());
        model.addAttribute("is500", status == HttpStatus.INTERNAL_SERVER_ERROR.value());

        return "error";
    }
}
