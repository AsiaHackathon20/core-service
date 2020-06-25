package org.hack20.coreservice.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Objects;

@Data
public class PersonActivityDetails {

    private String smpIdentifier;
    private String name;
    private String sid;
    private ZonedDateTime activityTimeStamp;

    public static PersonActivityDetails buildPerson(final String sid, final String name, final String smpIdentifier, final ZonedDateTime activityTimeStamp){
        final PersonActivityDetails personActivityDetails = new PersonActivityDetails();
        personActivityDetails.sid = sid;
        personActivityDetails.name = name;
        personActivityDetails.smpIdentifier = smpIdentifier;
        personActivityDetails.activityTimeStamp = activityTimeStamp;
        return personActivityDetails;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonActivityDetails personActivityDetails = (PersonActivityDetails) o;
        return name.equals(personActivityDetails.name) &&
                sid.equals(personActivityDetails.sid) &&
                smpIdentifier.equals(personActivityDetails.smpIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sid, smpIdentifier);
    }
}
