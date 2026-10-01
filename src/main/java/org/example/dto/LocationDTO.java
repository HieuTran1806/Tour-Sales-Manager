package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDTO {
    String idLocation;
    String locationName;
    String address;
    String nation;

    @Override
    public String toString(){
        return idLocation + " - " + locationName;
    }
}