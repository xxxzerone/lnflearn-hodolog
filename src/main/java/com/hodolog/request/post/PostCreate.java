package com.hodolog.request.post;

import com.hodolog.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreate {

    @NotBlank(message = "타이틀을 입력하세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
