package pl.taskyers.restauranty.core.data.menu.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "single_menu_dish")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({ "id", "name", "description", "price", "type" })
public class SingleMenuDish implements Serializable {
    
    private static final long serialVersionUID = -5939377399566867504L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Double price;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DishType type;
    
}
