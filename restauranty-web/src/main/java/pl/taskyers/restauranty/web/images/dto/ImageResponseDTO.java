package pl.taskyers.restauranty.web.images.dto;

import lombok.Value;

@Value
public class ImageResponseDTO {
    
    String name;
    
    String type;
    
    long size;
    
    boolean isMain;
    
}
