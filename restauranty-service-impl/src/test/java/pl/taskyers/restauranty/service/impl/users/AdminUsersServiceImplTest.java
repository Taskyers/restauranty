package pl.taskyers.restauranty.service.impl.users;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.users.entity.*;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;
import pl.taskyers.restauranty.repository.users.UserRepository;

import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;

public class AdminUsersServiceImplTest {
    
    private UserRepository userRepository;
    
    private AdminUsersServiceImpl adminUsersService;
    
    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        adminUsersService = new AdminUsersServiceImpl(userRepository);
    }
    
    @Test
    public void testGettingAllUsersExceptAdmin() {
        // given
        final Role roleClient = new Role(1L, RoleType.ROLE_CLIENT);
        final Role roleRestaurant = new Role(2L, RoleType.ROLE_RESTAURANT);
        final Role roleAdmin = new Role(3L, RoleType.ROLE_ADMIN);
        final UserBase userClient = new UserClient();
        userClient.setRole(roleClient);
        final UserBase userRestaurant = new UserRestaurant();
        userRestaurant.setRole(roleRestaurant);
        final UserBase userAdmin = new UserAdmin();
        userAdmin.setRole(roleAdmin);
        when(userRepository.findAll()).thenReturn(Lists.newArrayList(userClient, userRestaurant, userAdmin));
        
        // when
        final Collection<UserBase> result = adminUsersService.getUsers();
        
        // then
        assertThat(result, allOf(iterableWithSize(2), hasItems(userClient, userRestaurant)));
    }
    
    @Test
    public void testBanningUser() {
        // given
        final long id = 1L;
        final UserBase userBase = new UserClient();
        userBase.setEnabled(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(userBase));
        when(userRepository.save(userBase)).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final UserBase result = adminUsersService.banUser(id);
        
        // then
        assertThat(result.isEnabled(), is(false));
    }
    
    @Test
    public void testUnbanningUser() {
        // given
        final long id = 1L;
        final UserBase userBase = new UserClient();
        userBase.setEnabled(false);
        when(userRepository.findById(id)).thenReturn(Optional.of(userBase));
        when(userRepository.save(userBase)).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final UserBase result = adminUsersService.unbanUser(id);
        
        // then
        assertThat(result.isEnabled(), is(true));
    }
    
}