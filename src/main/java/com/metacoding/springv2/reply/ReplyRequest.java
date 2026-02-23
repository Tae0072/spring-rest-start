package com.metacoding.springv2.reply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class ReplyRequest {

    @Data
    public static class SaveDTO {
        @NotBlank(message = "댓글을 입력해주세요")
        @Size(min = 1, max = 100, message = "댓글은 1자 이상 100자 이하여야 합니다")
        private String comment;
    }

    @Data
    public static class UpdateDTO {
        @NotBlank(message = "댓글을 입력해주세요")
        @Size(min = 1, max = 100, message = "댓글은 1자 이상 100자 이하여야 합니다")
        private String comment;
    }
}
