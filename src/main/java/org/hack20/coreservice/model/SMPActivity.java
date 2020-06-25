package org.hack20.coreservice.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SMPActivity {

    private Set<PersonActivityDetails> totalNumberOfContacts = new HashSet<>();
    private Set<PersonActivityDetails> totalUnacceptableContacts = new HashSet<>();
    private Set<TextMessageDetails> textMessageDetails = new HashSet<>();

}
