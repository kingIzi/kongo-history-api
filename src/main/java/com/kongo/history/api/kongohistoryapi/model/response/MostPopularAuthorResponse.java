package com.kongo.history.api.kongohistoryapi.model.response;

import java.util.List;

import lombok.Data;

@Data
public class MostPopularAuthorResponse {
    private String authorFullName;
    private Integer numberOfBooks;
    private List<String> popularComics;
}
