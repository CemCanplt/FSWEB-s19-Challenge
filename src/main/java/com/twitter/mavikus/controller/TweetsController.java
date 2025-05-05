package com.twitter.mavikus.controller;

import com.twitter.mavikus.service.TweetsService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/tweet")
@AllArgsConstructor
@RestController
@Validated
public class TweetsController {

    private final TweetsService tweetsService;
    // Bu genelde bir service olur.

}
