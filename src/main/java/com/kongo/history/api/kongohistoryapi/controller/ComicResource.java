//package com.kunal52.springandfirebaseauth.controller;

package com.kongo.history.api.kongohistoryapi.controller;

import com.kongo.history.api.kongohistoryapi.auth.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("comic")
public class ComicResource {

    @GetMapping("create")
    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

}
