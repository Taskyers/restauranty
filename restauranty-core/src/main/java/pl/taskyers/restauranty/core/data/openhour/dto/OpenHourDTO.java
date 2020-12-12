package pl.taskyers.restauranty.core.data.open_hour.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenHourDTO {
    
    private String dayOfWeek;
    
    private String openTime;
    
    private String closeTime;
    
}
