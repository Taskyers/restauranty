package pl.taskyers.restauranty.service.impl.chat;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.chat.ChatMessageNotFoundException;
import pl.taskyers.restauranty.core.data.chat.dto.ChatMessageDTO;
import pl.taskyers.restauranty.core.data.chat.entity.ChatMessage;
import pl.taskyers.restauranty.core.data.chat.entity.ChatRoom;
import pl.taskyers.restauranty.core.data.chat.entity.MessageStatus;
import pl.taskyers.restauranty.core.data.users.entity.Role;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;
import pl.taskyers.restauranty.core.error.exceptions.ForbiddenException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.repository.chat.ChatMessageRepository;
import pl.taskyers.restauranty.repository.users.UserRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.service.chat.ChatRoomService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ChatMessageServiceImplTest {
    
    private ChatMessageRepository chatMessageRepository;
    
    private UserRepository userRepository;
    
    private ChatRoomService chatRoomService;
    
    private AuthProvider authProvider;
    
    private ChatMessageServiceImpl chatMessageService;
    
    @BeforeEach
    public void setup() {
        chatMessageRepository = mock(ChatMessageRepository.class);
        userRepository = mock(UserRepository.class);
        chatRoomService = mock(ChatRoomService.class);
        authProvider = mock(AuthProvider.class);
        chatMessageService = new ChatMessageServiceImpl(chatMessageRepository, userRepository, chatRoomService, authProvider);
    }
    
    @Test
    public void testSendMessageWithWrongAuthor() {
        //given
        UserBase user = new UserClient();
        user.setUsername("test");
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setAuthor("bad");
        when(authProvider.getUserEntity()).thenReturn(user);
        
        //when
        final ForbiddenException result =
                assertThrows(ForbiddenException.class, () -> chatMessageService.sendMessage(chatMessageDTO));
        
        //then
        assertThat(result.getMessage(), is("Chat message is not from you"));
    }
    
    @Test
    public void testSendMessageWithNotExistingRecipient() {
        //given
        String badUsername = "bad";
        UserBase user = new UserClient();
        user.setUsername("test");
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setAuthor(user.getUsername());
        chatMessageDTO.setRecipient(badUsername);
        when(authProvider.getUserEntity()).thenReturn(user);
        when(userRepository.findByUsername(chatMessageDTO.getRecipient())).thenReturn(Optional.empty());
        
        //when
        final ChatMessageNotFoundException result =
                assertThrows(ChatMessageNotFoundException.class, () -> chatMessageService.sendMessage(chatMessageDTO));
        
        //then
        assertThat(result.getMessage(), is("Chat message recipient with username " + badUsername + " not found"));
    }
    
    @Test
    public void testSendMessageWithBlankContentAndWrongRecipientRole() {
        //given
        Role role = new Role();
        role.setRole(RoleType.ROLE_CLIENT);
        UserBase user = new UserClient();
        user.setUsername("test");
        user.setRole(role);
        UserBase wrongUser = new UserClient();
        wrongUser.setRole(role);
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setAuthor(user.getUsername());
        chatMessageDTO.setRecipient("bad");
        when(authProvider.getUserEntity()).thenReturn(user);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(wrongUser));
        
        //when
        final ValidationException result =
                assertThrows(ValidationException.class, () -> chatMessageService.sendMessage(chatMessageDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(2));
    }
    
    @Test
    public void testSendValidMessage() {
        //given
        Role role = new Role();
        role.setRole(RoleType.ROLE_CLIENT);
        UserBase author = new UserClient();
        author.setUsername("test");
        author.setRole(role);
        UserBase recipient = new UserClient();
        Role anotherRole = new Role();
        anotherRole.setRole(RoleType.ROLE_RESTAURANT);
        recipient.setRole(anotherRole);
        recipient.setUsername("second");
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setAuthor(author.getUsername());
        chatMessageDTO.setRecipient(recipient.getUsername());
        chatMessageDTO.setContent("test");
        when(authProvider.getUserEntity()).thenReturn(author);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(recipient));
        when(chatRoomService.getChatRoom(any(UserBase.class),any(UserBase.class))).thenReturn(Optional.of(new ChatRoom()));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        //when
        final ChatMessage result = chatMessageService.sendMessage(chatMessageDTO);
        
        //then
        assertThat(result, notNullValue());
        assertThat(result.getAuthor(), is(author));
        assertThat(result.getRecipient(),is(recipient));
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }
    
    @Test
    public void testGetChatMessagesWithNotExistingRecipient() {
        //given
        String badUsername = "bad";
        UserBase user = new UserClient();
        user.setUsername("test");
        when(authProvider.getUserEntity()).thenReturn(user);
        when(userRepository.findByUsername(badUsername)).thenReturn(Optional.empty());
        
        //when
        final ChatMessageNotFoundException result =
                assertThrows(ChatMessageNotFoundException.class, () -> chatMessageService.getChatMessages(badUsername));
        
        //then
        assertThat(result.getMessage(), is("Chat message recipient with username " + badUsername + " not found"));
    }
    
    @Test
    public void testGetChatMessages() {
        //given
        String content = "test";
        Role role = new Role();
        role.setRole(RoleType.ROLE_CLIENT);
        UserBase loggedInUser = new UserClient();
        loggedInUser.setUsername("test");
        loggedInUser.setRole(role);
        UserBase recipient = new UserRestaurant();
        recipient.setUsername("second user");
        ChatMessage message = ChatMessage.builder()
                .author(loggedInUser)
                .recipient(recipient)
                .content(content)
                .timestamp(new Date())
                .build();
        when(authProvider.getUserEntity()).thenReturn(loggedInUser);
        when(userRepository.findByUsername(recipient.getUsername())).thenReturn(Optional.of(recipient));
        when(chatRoomService.getChatRoomMessages(any(UserBase.class), any(UserBase.class))).thenReturn(Sets.newHashSet(message));
        
        //when
        final List<ChatMessageDTO> result = chatMessageService.getChatMessages(recipient.getUsername());
        
        //then
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getAuthor(), is(loggedInUser.getUsername()));
        assertThat(result.get(0).getRecipient(), is(recipient.getUsername()));
        assertThat(result.get(0).getContent(), is(content));
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }
    
    
    @Test
    public void testCountChatMessagesWithNotExistingAuthor() {
        //given
        String badUsername = "bad";
        UserBase user = new UserClient();
        user.setUsername("test");
        when(authProvider.getUserEntity()).thenReturn(user);
        when(userRepository.findByUsername(badUsername)).thenReturn(Optional.empty());
        
        //when
        final ChatMessageNotFoundException result =
                assertThrows(ChatMessageNotFoundException.class, () -> chatMessageService.countNewMessages(badUsername));
        
        //then
        assertThat(result.getMessage(), is("Chat message author with username " + badUsername + " not found"));
    }
    
    @Test
    public void testCountChatMessagesWithExistingRecipient() {
        //given
        UserBase loggedInUser = new UserClient();
        loggedInUser.setUsername("test");
        UserBase author = new UserRestaurant();
        author.setUsername("author");
        when(authProvider.getUserEntity()).thenReturn(loggedInUser);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(author));
        when(chatMessageRepository.countByAuthorAndRecipientAndStatus(author,loggedInUser, MessageStatus.RECEIVED)).thenReturn(2L);
        
        //when
        final Long result = chatMessageService.countNewMessages(author.getUsername());
        
        //then
        assertThat(result, is(2L));
    }
    
}
