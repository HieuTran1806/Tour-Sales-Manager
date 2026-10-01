package org.example.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourTypeDTO {
    String idTourType;
    String typeOfTour;
    String description;
    String status;

    @Override
    public String toString(){
        return idTourType + " - " + typeOfTour;
    }
}
