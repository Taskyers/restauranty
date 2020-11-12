package pl.taskyers.restauranty.service.impl.chat;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.chat.ChatRoomNotFoundException;
import pl.taskyers.restauranty.core.data.chat.dto.ChatRoomDTO;
import pl.taskyers.restauranty.core.data.chat.entity.ChatMessage;
import pl.taskyers.restauranty.core.data.chat.entity.ChatRoom;
import pl.taskyers.restauranty.core.data.users.entity.Role;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.repository.chat.ChatRoomRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChatRoomServiceImplTest {
    
    private ChatRoomRepository chatRoomRepository;
    
    private AuthProvider authProvider;
    
    private ChatRoomServiceImpl chatRoomService;
    
    
    @BeforeEach
    public void setup() {
        chatRoomRepository = mock(ChatRoomRepository.class);
        authProvider = mock(AuthProvider.class);
        chatRoomService = new ChatRoomServiceImpl(chatRoomRepository, authProvider);
    }
    
    @Test
    public void testGetChatRoomForUserWithTheSameRole() {
        //given
        Role role = new Role();
        role.setRole(RoleType.ROLE_CLIENT);
        UserBase userClient = new UserClient();
        userClient.setRole(role);
        UserBase userSecondClient = new UserClient();
        userSecondClient.setRole(role);
        
        //when
        final ValidationException result =
                assertThrows(ValidationException.class, () -> chatRoomService.getChatRoom(userClient, userSecondClient));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(1));
    }
    
    @Test
    public void testGetExistingChatRoom() {
        //given
        Role clientRole = new Role();
        clientRole.setRole(RoleType.ROLE_CLIENT);
        Role restaurantRole = new Role();
        restaurantRole.setRole(RoleType.ROLE_RESTAURANT);
        UserBase userClient = new UserClient();
        userClient.setRole(clientRole);
        UserBase userRestaurant = new UserRestaurant();
        userRestaurant.setRole(restaurantRole);
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setClient(userClient);
        chatRoom.setRestaurant(userRestaurant);
        when(chatRoomRepository.findByClientAndRestaurant(userClient, userRestaurant)).thenReturn(Optional.of(chatRoom));
        
        //when
        final ChatRoom result = chatRoomService.getChatRoom(userClient, userRestaurant).get();
        
        //then
        assertThat(result, notNullValue());
        assertThat(result.getClient(), is(userClient));
        assertThat(result.getRestaurant(), is(userRestaurant));
    }
    
    @Test
    public void testGetNotExistingChatRoom() {
        //given
        Role clientRole = new Role();
        clientRole.setRole(RoleType.ROLE_CLIENT);
        Role restaurantRole = new Role();
        restaurantRole.setRole(RoleType.ROLE_RESTAURANT);
        UserBase userClient = new UserClient();
        userClient.setRole(clientRole);
        UserBase userRestaurant = new UserRestaurant();
        userRestaurant.setRole(restaurantRole);
        when(chatRoomRepository.findByClientAndRestaurant(userClient, userRestaurant)).thenReturn(Optional.empty());
        when(chatRoomRepository.save(any(ChatRoom.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        //when
        final ChatRoom result = chatRoomService.getChatRoom(userClient, userRestaurant).get();
        
        //then
        assertThat(result, notNullValue());
        assertThat(result.getClient(), is(userClient));
        assertThat(result.getRestaurant(), is(userRestaurant));
        verify(chatRoomRepository).save(any(ChatRoom.class));
    }
    
    @Test
    public void testGetNotExistingChatRoomMessages() {
        //given
        String username = "Test";
        UserBase userClient = new UserClient();
        userClient.setUsername(username);
        UserBase userRestaurant = new UserRestaurant();
        userRestaurant.setUsername(username);
        when(chatRoomRepository.findByClientAndRestaurant(userClient, userRestaurant)).thenReturn(Optional.empty());
        
        //when
        final ChatRoomNotFoundException result =
                assertThrows(ChatRoomNotFoundException.class, () -> chatRoomService.getChatRoomMessages(userClient, userRestaurant));
        
        //then
        assertThat(result.getMessage(), is("There are no chat messages between " + username + " and " + username));
    }
    
    @Test
    public void testGetExistingChatRoomMessages() {
        //given
        UserBase userClient = new UserClient();
        UserBase userRestaurant = new UserRestaurant();
        ChatMessage message = new ChatMessage();
        ChatMessage anotherMessage = new ChatMessage();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setMessages(Sets.newHashSet(message, anotherMessage));
        when(chatRoomRepository.findByClientAndRestaurant(userClient, userRestaurant)).thenReturn(Optional.of(chatRoom));
        
        //when
        final Set<ChatMessage> result = chatRoomService.getChatRoomMessages(userClient, userRestaurant);
        
        //then
        assertThat(result, notNullValue());
        assertThat(result, is(chatRoom.getMessages()));
    }
    
    @Test
    public void testGetCurrentUserChatRooms() {
        //given
        UserBase userClient = new UserClient();
        userClient.setId(1L);
        when(authProvider.getUserEntity()).thenReturn(userClient);
        when(chatRoomRepository.findChatsByUserId(userClient.getId())).thenReturn(new ArrayList());
        
        //when
        final List<ChatRoomDTO> result = chatRoomService.getCurrentUserChatRooms();
        
        //then
        assertThat(result, notNullValue());
        assertThat(result.size(), is(0));
    }
    
}
