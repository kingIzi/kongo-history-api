package com.kongo.history.api.kongohistoryapi.utils;


import lombok.Data;

@Data
public class ValueDataException extends Exception {
    private String code;

    public ValueDataException(String message) {
        super(message);
        this.code = "";
    }

    public ValueDataException(String message, String code) {
        super(message);
        this.code = code;
    }

    public static final String itemNotFoundErrorMsg(final String item,final String documentId){
        return "Item " + item + " Not found in database. \n DocumentId = " + documentId;
    }

    public static final String itemNotCreatedMsg(final String item){
        return "Failed to save item " + item + " to database.";
    }

}