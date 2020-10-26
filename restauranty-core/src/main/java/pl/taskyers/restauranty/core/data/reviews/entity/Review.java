package pl.taskyers.restauranty.core.data.reviews.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.reviews.enums.ReviewRate;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review implements Serializable {
    
    private static final long serialVersionUID = 162950349047247674L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(targetEntity = UserBase.class)
    @JoinColumn(name = "`user`")
    private UserBase user;
    
    @ManyToOne(targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant")
    private Restaurant restaurant;
    
    @Column(nullable = false)
    private String content;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewRate rate;
    
}
