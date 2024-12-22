package com.hodolog.repository.post;

import com.hodolog.domain.Post;
import com.hodolog.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
