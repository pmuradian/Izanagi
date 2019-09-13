package main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import main.services.*;

@RestController("")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody UserSpec userSpec) {
        return userService.createUser(userSpec);
    }

    @DeleteMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

    @GetMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PatchMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable String id, @RequestBody UserSpec userSpec) {
        return userService.updateUser(id, userSpec);
    }
}