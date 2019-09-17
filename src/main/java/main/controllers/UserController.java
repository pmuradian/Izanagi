package main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.services.*;

@RestController("")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/users")
    public ResponseEntity<Result<User>> createUser(@RequestBody UserSpec userSpec) {
        Result<User> result;
        ValidationResult validationResult = userSpec.validate();

        if (!validationResult.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (String message: validationResult.getMessages()) {
                messageBuilder.append(message).append("\n");
            }
            result = new Result<>(null, StatusCodes.INVALID_SPEC, messageBuilder.toString());
        } else {
            result = userService.createUser(userSpec);
        }

        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }

    @PatchMapping(value = "/users/{id}")
    public ResponseEntity<Result<User>> updateUser(@PathVariable String id, @RequestBody UserSpec userSpec) {
        Result<User> result;
        ValidationResult validationResult = userSpec.validate();

        if (!validationResult.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (String message: validationResult.getMessages()) {
                messageBuilder.append(message).append("\n");
            }
            result = new Result<>(null, StatusCodes.INVALID_SPEC, messageBuilder.toString());
        } else {
            result = userService.updateUser(id, userSpec);
        }

        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Result<Boolean>> deleteUser(@PathVariable String id) {
        Result<Boolean> result = userService.deleteUser(id);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<Result<User>> getUser(@PathVariable String id) {
        Result<User> result = userService.getUser(id);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }
}