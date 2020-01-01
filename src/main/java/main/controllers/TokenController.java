package main.controllers;

import main.models.Result;
import main.services.TokenService;
import main.specs.TokenSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController("")
public class TokenController {
    @Autowired
    private TokenService service;

    @PostMapping(value="/tokens/get")
    public ResponseEntity<Result<String>> getToken(@RequestBody TokenSpec tokenSpec) {
        Result<String> token = service.getToken(tokenSpec.getLogin(), tokenSpec.getPassword());
        HttpStatus httpStatus = HttpStatus.resolve(token.getStatusCode().getCode());
        ResponseEntity responseEntity = new ResponseEntity<Result<String>>(token, httpStatus);
        return responseEntity;
    }

    @PostMapping(value = "/tokens/delete")
    public ResponseEntity<Void> delete(@RequestHeader String header) {
        ResponseEntity<Void> responseEntity;
        Result<Boolean> result = service.deleteToken(header);
        responseEntity = new ResponseEntity<>(HttpStatus.resolve(result.getStatusCode().getCode()));
        return responseEntity;
    }
}
