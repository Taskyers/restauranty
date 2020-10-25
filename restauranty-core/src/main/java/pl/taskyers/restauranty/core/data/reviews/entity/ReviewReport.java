package pl.taskyers.restauranty.core.data.reviews.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "review_report")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewReport implements Serializable {
    
    private static final long serialVersionUID = 7166849901262885764L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(targetEntity = Review.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review")
    private Review review;
    
    @ManyToOne(targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant")
    private Restaurant restaurant;
    
}
