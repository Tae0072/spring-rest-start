package com.metacoding.springv2.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class BoardRequest {

    @Data
    public static class SaveDTO {
        @NotBlank(message = "제목을 입력해주세요")
        @Size(min = 1, max = 30, message = "제목은 1자 이상 30자 이하여야 합니다")
        private String title;

        @NotBlank(message = "내용을 입력해주세요")
        @Size(min = 1, max = 300, message = "내용은 1자 이상 300자 이하여야 합니다")
        private String content;
    }

    @Data
    public static class UpdateDTO {
        @NotBlank(message = "제목을 입력해주세요")
        @Size(min = 1, max = 30, message = "제목은 1자 이상 30자 이하여야 합니다")
        private String title;

        @NotBlank(message = "내용을 입력해주세요")
        @Size(min = 1, max = 300, message = "내용은 1자 이상 300자 이하여야 합니다")
        private String content;
    }
}
