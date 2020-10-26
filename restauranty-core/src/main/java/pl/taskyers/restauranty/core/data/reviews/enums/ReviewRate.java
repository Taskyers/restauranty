package pl.taskyers.restauranty.core.data.reviews.enums;

import pl.taskyers.restauranty.core.data.reviews.ReviewRateNotFoundException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;

import java.util.Arrays;

public enum ReviewRate {
    
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);
    
    private final int value;
    
    ReviewRate(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static ReviewRate getByIntValue(int value) {
        return Arrays.stream(values())
                .filter(reviewRate -> reviewRate.value == value)
                .findFirst()
                .orElseThrow(() -> new ReviewRateNotFoundException(MessageProvider.getMessage(MessageCode.Review.RATE_NOT_FOUND, value)));
    }
    
}
