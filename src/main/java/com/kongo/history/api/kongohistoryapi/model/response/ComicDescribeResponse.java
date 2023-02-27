package com.kongo.history.api.kongohistoryapi.model.response;

import com.google.firebase.database.utilities.Pair;
import lombok.Data;

import java.util.List;

@Data
public class ComicDescribeResponse {
    private Double views;
    private Double likes;
    private Double comments;
    private List<Pair<String,Double>> chartData;
}
