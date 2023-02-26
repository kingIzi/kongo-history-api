package com.kongo.history.api.kongohistoryapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import com.kongo.history.api.kongohistoryapi.model.entity.Comic;
import com.kongo.history.api.kongohistoryapi.model.entity.Comment;
import com.kongo.history.api.kongohistoryapi.model.form.AddCommentForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindCommentForm;
import com.kongo.history.api.kongohistoryapi.model.form.LikeCommentForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindCommentForm;
import com.kongo.history.api.kongohistoryapi.repository.CommentRepository;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.utils.UtilityFormatter;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import com.kongo.history.api.kongohistoryapi.repository.ComicRepository;

import java.util.Map;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Service
public class CommentService {

    @Autowired
    private ComicRepository comicRepository;

    public HttpDataResponse<?> addComment(final String comicId,
            final AddCommentForm addCommentForm) {
        final var httpDataResponse = new HttpDataResponse<Comic>();
        try {
            final var comic = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));

            final var comment = new Comment(addCommentForm.getEmail(), addCommentForm.getMessage());
            comic.getComments().add(comment);
            Map<String, Object> update = new HashMap<>();
            update.put(Comic.COMMENTS, comic.getComments());
            final var commentedComic = this.comicRepository.save(comicId, update);
            final var res = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));
            httpDataResponse.setResponse(res);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, "Failed to add comment",
                    AppConst._KEY_CODE_INTERNAL_ERROR);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<?> removeComment(final String comicId, AddCommentForm addCommentForm) {
        final var httpDataResponse = new HttpDataResponse<Comic>();
        try {
            final var comic = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));

            final var comment = new Comment(addCommentForm.getEmail(), addCommentForm.getMessage());
            comic.getComments().remove(comment);
            Map<String, Object> update = new HashMap<>();
            update.put(Comic.COMMENTS, comic.getComments());
            final var commentedComic = this.comicRepository.save(comicId, update);
            final var res = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));
            httpDataResponse.setResponse(res);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, "Failed to add comment",
                    AppConst._KEY_CODE_INTERNAL_ERROR);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<?> replyComment(final String comicId, final FindCommentForm findCommentForm) {
        final var httpDataResponse = new HttpDataResponse<Comic>();
        try {
            final var comic = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found",
                            AppConst._KEY_CODE_PARAMS_ERROR));
            final var comment = this.findComment(comicId, findCommentForm).getResponse();
            if (comment == null)
                throw new ValueDataException("Comment not found", AppConst._KEY_CODE_PARAMS_ERROR);
            if (comment.getReplies() == null)
                comment.setReplies(new ArrayList<>());

            final var index = comic.getComments().indexOf(comment);
            comment.getReplies().add(findCommentForm.getComment());
            comic.getComments().set(index, comment);
            Map<String, Object> update = new HashMap<>();
            update.put(Comic.COMMENTS, comic.getComments());
            final var commentedComic = this.comicRepository.save(comicId, update);
            final var res = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found",
                            AppConst._KEY_CODE_PARAMS_ERROR));
            httpDataResponse.setResponse(res);

        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, "Failed to add comment",
                    AppConst._KEY_CODE_INTERNAL_ERROR);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<?> removeComment(final String comicId, final FindCommentForm findCommentForm) {
        final var httpDataResponse = new HttpDataResponse<Comic>();
        try {
            final var comic = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));

            final var commentIndex = comic.getComments()
                    .indexOf(new Comment(findCommentForm.getEmail(), findCommentForm.getMessage()));

            if (commentIndex == -1)
                throw new ValueDataException("Comment not found", AppConst._KEY_CODE_PARAMS_ERROR);

            if (findCommentForm.getComment() != null) {
                final var replyIndex = comic.getComments().get(commentIndex).getReplies()
                        .indexOf(findCommentForm.getComment());
                comic.getComments().get(commentIndex).getReplies().remove(replyIndex);
            }
            if (findCommentForm.getComment() == null) {
                comic.getComments().remove(commentIndex);
            }
            Map<String, Object> update = new HashMap<>();
            update.put(Comic.COMMENTS, comic.getComments());
            final var commentedComic = this.comicRepository.save(comicId, update);
            final var res = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found",
                            AppConst._KEY_CODE_PARAMS_ERROR));
            httpDataResponse.setResponse(res);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, "Failed to add comment",
                    AppConst._KEY_CODE_INTERNAL_ERROR);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<Comment> findComment(final String comicId, final FindCommentForm findCommentForm) {
        final var httpDataResponse = new HttpDataResponse<Comment>();
        try {
            final var comic = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));

            final var find = new Comment(findCommentForm.getEmail(), findCommentForm.getMessage());
            final var found = comic.getComments().stream().filter(e -> e.equals(find)).findFirst()
                    .orElseThrow(AppUtilities.supplyException("Comment not found", AppConst._KEY_CODE_PARAMS_ERROR));
            httpDataResponse.setResponse(found);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<?> likeComment(final String userId, final String comicId,
            final LikeCommentForm likeCommentForm) {
        final var httpDataResponse = new HttpDataResponse<Comic>();
        try {
            final var comic = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));

            final var commentIndex = comic.getComments()
                    .indexOf(new Comment(likeCommentForm.getEmail(), likeCommentForm.getMessage()));

            if (commentIndex == -1)
                throw new ValueDataException("Comment not found", AppConst._KEY_CODE_PARAMS_ERROR);

            if (likeCommentForm.getComment() != null) {
                final var replyIndex = comic.getComments().get(commentIndex).getReplies()
                        .indexOf(likeCommentForm.getComment());
                try {
                    if (comic.getComments().get(commentIndex).getReplies().get(replyIndex).getLikes().contains(userId))
                        comic.getComments().get(commentIndex).getReplies().get(replyIndex).getLikes().remove(userId);
                    else
                        comic.getComments().get(commentIndex).getReplies().get(replyIndex).getLikes().add(userId);

                } catch (NullPointerException e) {
                    comic.getComments().get(commentIndex).getReplies().get(replyIndex).setLikes(new ArrayList<>());
                    comic.getComments().get(commentIndex).getReplies().get(replyIndex).getLikes().add(userId);
                }
            }
            if (likeCommentForm.getComment() == null) {
                if (comic.getComments().get(commentIndex).getLikes().contains(userId))
                    comic.getComments().get(commentIndex).getLikes().remove(userId);
                else
                    comic.getComments().get(commentIndex).getLikes().add(userId);
            }
            Map<String, Object> update = new HashMap<>();
            update.put(Comic.COMMENTS, comic.getComments());
            final var commentedComic = this.comicRepository.save(comicId, update);
            final var res = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found",
                            AppConst._KEY_CODE_PARAMS_ERROR));
            httpDataResponse.setResponse(res);
        } catch (ValueDataException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpDataResponse;
    }
}
