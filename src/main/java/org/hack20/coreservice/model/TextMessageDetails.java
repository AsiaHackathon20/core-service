package org.hack20.coreservice.model;

import lombok.Data;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
@Data
public class TextMessageDetails {

    private String fromUserName;
    private String toUserName;
    private String text;
    private ZonedDateTime createTimeStamp;

    public static TextMessageDetails build(final String fromUserName, final String toUserName, final String text, final long createTimeStampInSeconds){
        final TextMessageDetails textMessageDetails = new TextMessageDetails();
        textMessageDetails.fromUserName = fromUserName;
        textMessageDetails.toUserName = toUserName;
        textMessageDetails.text = text;
        textMessageDetails.createTimeStamp = ZonedDateTime.ofInstant(Instant.ofEpochSecond(createTimeStampInSeconds), ZoneOffset.UTC);
        return textMessageDetails;
    }
}
