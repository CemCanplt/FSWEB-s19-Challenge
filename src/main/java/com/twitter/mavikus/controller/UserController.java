package com.twitter.mavikus.controller;

import com.twitter.mavikus.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

// @RequestMapping("/REQUEST_URL")
@AllArgsConstructor
@RestController
@Validated
public class UserController {

    private final UserService userService;
    // Bu genelde bir service olur.

}
