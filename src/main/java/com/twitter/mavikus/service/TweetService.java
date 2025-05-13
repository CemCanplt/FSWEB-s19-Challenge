package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.tweet.TweetCreateDTO;
import com.twitter.mavikus.dto.tweet.TweetDeleteResponseDTO;
import com.twitter.mavikus.dto.tweet.TweetResponseDTO;
import com.twitter.mavikus.dto.tweet.TweetUpdateDTO;
import com.twitter.mavikus.dto.tweet.TweetUpdateResponseDTO;
import com.twitter.mavikus.dto.tweet.TweetSimpleDTO;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;

import java.util.List;
import java.util.Map;

public interface TweetService {
    // Core operations - keep these for internal service usage
    Tweet findById(long id);
    List<Tweet> findTweetsByUserId(long userId);
    
    // API-facing operations - these are directly used by controllers
    Tweet createTweet(TweetCreateDTO tweetDTO, User user);
    Map<String, Object> createTweetResponseMap(Tweet tweet, User user);
    TweetResponseDTO getTweetWithDetails(long tweetId);
    List<TweetSimpleDTO> findTweetSimpleDTOsByUserId(long userId);
    TweetUpdateResponseDTO updateTweetSimple(Long tweetId, TweetUpdateDTO tweetUpdateDTO, Long currentUserId);
    TweetDeleteResponseDTO deleteTweetAndReturnDTO(Long tweetId, Long currentUserId);
}
