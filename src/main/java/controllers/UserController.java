package controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import services.*;

@RestController("")
public class UserController {
    @RequestMapping(value = "/users")
    public User createUser(@RequestBody String userSpec) {
        return new User("a", "a", "a", "a");//UserService.createUser(userSpec);
    }


}