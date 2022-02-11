package com.devsuperior.dscatalog.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError{

    private List<FieldMessage> errors = new ArrayList<>();

    public void addError(String fieldName, String message){
        this.errors.add(new FieldMessage(fieldName, message));
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }
}
