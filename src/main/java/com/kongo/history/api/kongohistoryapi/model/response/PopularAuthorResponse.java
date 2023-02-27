package com.kongo.history.api.kongohistoryapi.model.response;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.entity.Comic;
import lombok.Data;

import java.util.List;

@Data
public class PopularAuthorResponse {
    private Author author;
    private List<Comic> comics;
}
