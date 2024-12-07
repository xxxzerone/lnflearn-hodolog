package com.hodolog.domain;

import lombok.Getter;

@Getter
public class PostEditor {

    private final String title;
    private final String content;

    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostEditor.PostEditorBuilder builder() {
        return new PostEditor.PostEditorBuilder();
    }

    public static class PostEditorBuilder {

        private String title;
        private String content;

        PostEditorBuilder() {
        }

        public PostEditor.PostEditorBuilder title(final String title) {
            if (title != null) {
                this.title = title;
            }
            return this;
        }

        public PostEditor.PostEditorBuilder content(final String content) {
            if (content != null) {
                this.content = content;
            }
            return this;
        }

        public PostEditor build() {
            return new PostEditor(title, content);
        }

        @Override
        public String toString() {
            return "PostEditor.PostEditorBuilder{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

}
