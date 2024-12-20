package com.hodolog.controller;

import com.hodolog.request.PostCreate;
import com.hodolog.request.PostEdit;
import com.hodolog.request.PostSearch;
import com.hodolog.response.PostResponse;
import com.hodolog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// SSR -> jsp, thymeleaf, mustache, freemarker
//      -> html rendering
// SPA -> vue
///     -> javascript + <-> API (JSON)
// vue -> vue+SSR = nuxt
// react -> react+SSR = next

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        log.info("request={}", request.toString());
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable("postId") Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable("postId") Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable("postId") Long postId) {
        postService.delete(postId);
    }
}
