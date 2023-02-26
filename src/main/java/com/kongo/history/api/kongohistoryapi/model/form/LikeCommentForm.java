package com.kongo.history.api.kongohistoryapi.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LikeCommentForm extends FindCommentForm {

    private boolean isReply;

    public LikeCommentForm() {
        this.isReply = this.getComment() == null;
    }

    public boolean getIsReply() {
        return this.isReply;
    }

    public void setIsReply(final boolean isReply) {
        this.isReply = isReply;
    }
}
