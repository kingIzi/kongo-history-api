package com.kongo.history.api.kongohistoryapi.validator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Validator<T> {
    private T data;

    public Validator(final T data){
        this.data = data;
    }
}

