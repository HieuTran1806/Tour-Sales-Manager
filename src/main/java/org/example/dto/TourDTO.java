package org.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDTO {
    String idTour;
    String tourName;
    int numberOfDate;
    BigDecimal price;
    int seats;
    String departureLocation;
    String imgLink;
    String idTourType;
    String idLocation;

    @Override
    public String toString(){
        return idTour;
    }
}
