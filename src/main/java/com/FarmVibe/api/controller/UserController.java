package com.FarmVibe.api.controller;

import com.FarmVibe.api.Dao.UserService;
import com.FarmVibe.api.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ConcurrentHashMap;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    // Temporary in-memory store to keep track of verified phones
    private ConcurrentHashMap<String, Boolean> verifiedPhones = new ConcurrentHashMap<>();

    @PostMapping("/send-otp")
    @ResponseBody
    public String sendOtp(@RequestParam("phone") String phone) {
        if (!phone.startsWith("+91")) {
            phone = "+91" + phone;
        }
        otpService.generateOtp(phone);
        // Remove any previous verification on new OTP generation
        verifiedPhones.remove(phone);
        return "OTP sent successfully";
    }

    @PostMapping("/verify-otp")
    @ResponseBody
    public String verifyOtp(@RequestParam("phone") String phone, @RequestParam("otp") String otp) {
        if (!phone.startsWith("+91")) {
            phone = "+91" + phone;
        }
        boolean isValid = otpService.validateOtp(phone, otp);
        if (isValid) {
            // Mark phone as verified
            verifiedPhones.put(phone, true);
        }
        System.out.println("OTP validation confirmation: " + isValid);
        return isValid ? "{\"success\":true}" : "{\"success\":false}";
    }

    @PostMapping("/user-register")
    public String registerUser(@RequestParam("firstName") String firstName,
                               @RequestParam("lastName") String lastName,
                               @RequestParam("email") String email,
                               @RequestParam("phone") String phone,
                               @RequestParam("password") String password,
                               @RequestParam("profileImage") MultipartFile profileImage) {
        if (!phone.startsWith("+91")) {
            phone = "+91" + phone;
        }

        // Check if OTP was verified earlier
        if (verifiedPhones.getOrDefault(phone, false)) {
            // OTP was verified, proceed to register user
            String result = userService.registerUser(firstName, lastName, email, Long.parseLong(phone.replace("+91", "")), password, profileImage);

            // After registration, remove the verification flag to avoid reuse
            verifiedPhones.remove(phone);

            // Return redirect or result based on your userService
            // Assuming userService.registerUser returns redirect string
            return result;
        } else {
            // OTP not verified yet
            return "redirect:/pageUrl?page=register&error=Invalid OTP";
        }
    }
}
