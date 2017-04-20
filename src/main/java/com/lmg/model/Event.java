package com.lmg.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event<T> {

    private String eventId;
    private DateTime timestamp;
    private String type;
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    private T data;

    public static <T extends EventData> Event<T> createNew(String type, T data) 
    {
    	Event<T> val = new Event<>();
    	val.eventId = UUID.randomUUID().toString();
    	val.timestamp = DateTime.now();
    	val.type = type;
    	val.data = data; 
        return val;
    }	
}
