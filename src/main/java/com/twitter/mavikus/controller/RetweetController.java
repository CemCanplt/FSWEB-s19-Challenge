package com.twitter.mavikus.controller;

import com.twitter.mavikus.service.RetweetService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/retweet")
@AllArgsConstructor
@RestController
@Validated
public class RetweetController {

    private final RetweetService retweetService;
    // Bu genelde bir service olur.

}
