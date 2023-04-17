//package com.kunal52.springandfirebaseauth.controller;

package com.kongo.history.api.kongohistoryapi.controller;

import com.kongo.history.api.kongohistoryapi.model.entity.Comic;
import com.kongo.history.api.kongohistoryapi.model.form.AddComicForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindComicForm;
import com.kongo.history.api.kongohistoryapi.model.form.UpdateComicForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindCommentForm;
import com.kongo.history.api.kongohistoryapi.model.form.AddCommentForm;
import com.kongo.history.api.kongohistoryapi.model.form.LikeCommentForm;
import com.kongo.history.api.kongohistoryapi.service.ComicService;
import com.kongo.history.api.kongohistoryapi.service.CommentService;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("comic")
public class ComicResource {

    @Autowired
    private ComicService comicService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/createOne")
    public HttpDataResponse<Comic> createComic(
            @RequestParam(value = "thumbnail", required = true) MultipartFile thumbnail,
            @RequestParam(value = "data", required = true) MultipartFile data,
            @Valid @ModelAttribute AddComicForm addComicForm) {
        return this.comicService.createComic(thumbnail, data, addComicForm);
    }

    @PutMapping("/updateOne")
    public HttpDataResponse<Comic> updateComic(@RequestParam(required = true) String comicId,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "data", required = false) MultipartFile data,
            @ModelAttribute UpdateComicForm updateComicForm) {
        return this.comicService.updateComic(comicId, thumbnail, data, updateComicForm);
    }

    @GetMapping("/list")
    public HttpDataResponse<List<Comic>> getComicList(@RequestParam(required = false) Integer limit) {
        return this.comicService.getComicList(limit);
    }

    @PostMapping("/list")
    public HttpDataResponse<List<Comic>> getComicList(@RequestParam(required = false) Integer limit,
            @RequestBody FindComicForm findComicForm) {
        return this.comicService.getComicList(limit, findComicForm);
    }

    @DeleteMapping("/delete")
    public HttpDataResponse<String> removeComic(@RequestParam(required = true) String comicId) {
        return this.comicService.removeComic(comicId);
    }

    @GetMapping("/findOne")
    public HttpDataResponse<Comic> findComic(@RequestParam(required = true) String comicId) {
        return this.comicService.findComic(comicId);
    }

    @GetMapping("/describe")
    public HttpDataResponse<?> comicDescribe(@RequestParam(required = false) Integer limit) {
        return this.comicService.comicDescribe(limit == null ? 1000 : limit);
    }

    @GetMapping("/mostPopular")
    public HttpDataResponse<?> mostPopular(@RequestParam(required = false) Integer limit) {
        return this.comicService.mostPopularAuthor(limit == null ? 1000 : limit);
    }

    @GetMapping("/video/like")
    public HttpDataResponse<?> likeComment(@RequestParam(required = true) final String userId,
            @RequestParam(required = true) final String comicId) {
        return null;
    }

    @GetMapping("/video/view")
    public HttpDataResponse<?> viewComic(@RequestParam(required = true) final String userId,
            @RequestParam(required = true) final String comicId) {
        return this.comicService.viewComic(userId, comicId);
    }

    @GetMapping("/comment/find")
    public HttpDataResponse<?> findComments(@RequestParam(required = true) final String comicId,
            @RequestBody final FindCommentForm findCommentForm) {
        return this.commentService.findComment(comicId, findCommentForm);
    }

    @PostMapping("/comment/reply")
    public HttpDataResponse<?> replyComment(@RequestParam(required = true) final String comicId,
            @RequestBody @Valid final FindCommentForm findCommentForm) {
        return this.commentService.replyComment(comicId, findCommentForm);
    }

    @PostMapping("/comment/add")
    public HttpDataResponse<?> addComment(@RequestParam(required = true) final String comicId,
            @RequestBody @Valid final AddCommentForm addCommentForm) {
        return this.commentService.addComment(comicId, addCommentForm);
    }

    @PutMapping("/comment/like")
    public HttpDataResponse<?> likeComment(@RequestParam(required = true) final String userId,
            @RequestParam(required = true) final String comicId, @RequestBody LikeCommentForm likeCommentForm) {
        return this.commentService.likeComment(userId, comicId, likeCommentForm);
    }

    @DeleteMapping("/comment/remove")
    public HttpDataResponse<?> removeComment(@RequestParam(required = true) final String comicId,
            final @RequestBody FindCommentForm findCommentForm) {
        return this.commentService.removeComment(comicId, findCommentForm);
    }
}
