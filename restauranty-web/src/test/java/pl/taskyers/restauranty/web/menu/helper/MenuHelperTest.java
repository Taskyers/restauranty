package pl.taskyers.restauranty.web.menu.helper;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;
import pl.taskyers.restauranty.web.menu.dto.MenuGroupWrapper;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;

public class MenuHelperTest {
    
    @Test
    public void testGroupingMenuForRestaurant() throws Exception {
        // given
        final List<SingleMenuDish> menu = getMenu("/menu_restaurant_test.csv");
        
        // when
        final MenuGroupWrapper result = MenuHelper.groupMenu(Sets.newHashSet(menu));
        
        // then
        assertThat(result.getGroups(), iterableWithSize(DishType.values().length));
        assertThat(result.getByDishType(DishType.SOUP), iterableWithSize(2));
        assertThat(result.getByDishType(DishType.BURGER), iterableWithSize(2));
        assertThat(result.getByDishType(DishType.PIZZA), iterableWithSize(2));
        assertThat(result.getByDishType(DishType.MAIN), iterableWithSize(5));
        assertThat(result.getByDishType(DishType.DESSERT), iterableWithSize(0));
        assertThat(result.getByDishType(DishType.DRINK), iterableWithSize(1));
        assertThat(result.getByDishType(DishType.COFFEE), iterableWithSize(2));
        assertThat(result.getByDishType(DishType.TEA), iterableWithSize(2));
        assertThat(result.getByDishType(DishType.SANDWICH), iterableWithSize(1));
        assertThat(result.getByDishType(DishType.SNACK), iterableWithSize(1));
        assertThat(result.getByDishType(DishType.SALAD), iterableWithSize(2));
    }
    
    private List<SingleMenuDish> getMenu(String name) throws Exception {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.schemaFor(SingleMenuDish.class)
                .withHeader()
                .withColumnSeparator(',');
        MappingIterator<SingleMenuDish> iterator = csvMapper.readerFor(SingleMenuDish.class)
                .with(csvSchema)
                .readValues(getClass().getResourceAsStream(name));
        return iterator.readAll();
    }
    
}