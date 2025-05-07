package com.twitter.mavikus.controller;

import com.twitter.mavikus.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/comment")
@AllArgsConstructor
@RestController
@Validated
public class CommentController {

    private final CommentService commentService;
    // Bu genelde bir service olur.

}
