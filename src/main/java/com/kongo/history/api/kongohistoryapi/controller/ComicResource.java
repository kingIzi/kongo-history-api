//package com.kunal52.springandfirebaseauth.controller;

package com.kongo.history.api.kongohistoryapi.controller;

import com.kongo.history.api.kongohistoryapi.auth.models.User;
import com.kongo.history.api.kongohistoryapi.model.entity.Comic;
import com.kongo.history.api.kongohistoryapi.model.form.AddComicForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindComicForm;
import com.kongo.history.api.kongohistoryapi.model.form.UpdateComicForm;
import com.kongo.history.api.kongohistoryapi.service.ComicService;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("comic")
public class ComicResource {

    @Autowired
    private ComicService comicService;

    @PostMapping("/createOne")
    public HttpDataResponse<Comic> createComic(@RequestParam(value = "thumbnail",required = true) MultipartFile thumbnail, @RequestParam(value = "data",required = true) MultipartFile data, @Valid @ModelAttribute AddComicForm addComicForm){
        return this.comicService.createComic(thumbnail,data,addComicForm);
    }

    @PutMapping("/updateOne")
    public HttpDataResponse<Comic> updateComic(@RequestParam(required = true) String comicId,@RequestParam(value = "thumbnail",required = false) MultipartFile thumbnail, @RequestParam(value = "data",required = false) MultipartFile data, @ModelAttribute UpdateComicForm updateComicForm){
        return this.comicService.updateComic(comicId,thumbnail,data,updateComicForm);
    }

    @GetMapping("/list")
    public HttpDataResponse<List<Comic>> getComicList(@RequestParam(required = false) Integer limit){
        return this.comicService.getComicList(limit);
    }

    @PostMapping("/list")
    public HttpDataResponse<List<Comic>> getComicList(@RequestParam(required = false) Integer limit,@RequestBody FindComicForm findComicForm){
        return new HttpDataResponse<>();
    }

    @DeleteMapping("/delete")
    public HttpDataResponse<String> removeComic(@RequestParam(required = true) String comicId){
        return this.comicService.removeComic(comicId);
    }

    @GetMapping("/findOne")
    public HttpDataResponse<Comic> findComic(@RequestParam(required = true) String comicId){
        return this.comicService.findComic(comicId);
    }
}
