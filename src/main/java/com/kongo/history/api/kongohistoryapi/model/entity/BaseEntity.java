package com.kongo.history.api.kongohistoryapi.model.entity;

import com.google.cloud.firestore.annotation.DocumentId;

import lombok.Data;

public abstract class BaseEntity {
    // public abstract void setId(String id){
    //     this.id = id;
    // }

    // public String getId(){
    //     return this.id;
    // }

    public abstract void setId(final String id);

    public abstract String getId();

}
