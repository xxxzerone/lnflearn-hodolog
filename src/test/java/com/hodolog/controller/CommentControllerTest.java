package com.hodolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.config.HodologMockUser;
import com.hodolog.domain.Comment;
import com.hodolog.domain.Post;
import com.hodolog.domain.User;
import com.hodolog.repository.UserRepository;
import com.hodolog.repository.comment.CommentRepository;
import com.hodolog.repository.post.PostRepository;
import com.hodolog.request.comment.CommentCreate;
import com.hodolog.request.comment.CommentDelete;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성")
    void test1() throws Exception {
        // given
        User user = User.builder()
                .name("호돌맨")
                .email("hodol@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .user(user)
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);

        CommentCreate request = CommentCreate.builder()
                .author("호순이")
                .password("123456")
                .content("댓글입니다. 아아아아아 10글자 제한")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(1L, commentRepository.count());

        Comment savedComment = commentRepository.findAll().get(0);
        assertEquals("호순이", savedComment.getAuthor());
        assertNotEquals("123456", savedComment.getPassword());
        assertTrue(passwordEncoder.matches("123456", savedComment.getPassword()));
        assertEquals("댓글입니다. 아아아아아 10글자 제한", savedComment.getContent());
    }

    @Test
    @DisplayName("댓글 삭제")
    void test2() throws Exception {
        // given
        User user = User.builder()
                .name("호돌맨")
                .email("hodol@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .user(user)
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);

        String encryptedPassword = passwordEncoder.encode("123456");

        Comment comment = Comment.builder()
                .author("호순이")
                .password(encryptedPassword)
                .content("댓글입니다. 아아아아아 10글자 제한")
                .build();
        comment.setPost(post);
        commentRepository.save(comment);

        CommentDelete request = new CommentDelete("123456");
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/comments/{commentId}/delete", comment.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

    }

}