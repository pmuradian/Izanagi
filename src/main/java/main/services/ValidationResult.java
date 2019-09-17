package main.services;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    private Map<String, String> validationResult = new HashMap<>();

    public void put(String field, String message) {
        validationResult.put(field, message);
    }

    public Map<String, String> getValidationResult() {
        return validationResult;
    }

    public Boolean isEmpty() {
        return validationResult.isEmpty();
    }

    public Iterable<String> getMessages() {
        return validationResult.values();
    }
}
