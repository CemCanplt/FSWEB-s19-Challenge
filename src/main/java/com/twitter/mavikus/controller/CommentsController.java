package com.twitter.mavikus.controller;

import com.twitter.mavikus.service.CommentsService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/comment")
@AllArgsConstructor
@RestController
@Validated
public class CommentsController {

    private final CommentsService commentsService;
    // Bu genelde bir service olur.

}
