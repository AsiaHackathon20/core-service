package org.hack20.coreservice.model;

import lombok.Data;

@Data
public class SmpUserContactsMapping {

    private PersonActivityDetails jpmUser;
    private PersonActivityDetails acceptableContact;
    private PersonActivityDetails unAcceptableContact;

    public static SmpUserContactsMapping build(final PersonActivityDetails jpmUser, final PersonActivityDetails acceptableContact, final PersonActivityDetails unAcceptableContact){
        final SmpUserContactsMapping smpUserContactsMapping = new SmpUserContactsMapping();
        smpUserContactsMapping.jpmUser = jpmUser;
        smpUserContactsMapping.acceptableContact = acceptableContact;
        smpUserContactsMapping.unAcceptableContact = unAcceptableContact;
        return smpUserContactsMapping;
    }
}
