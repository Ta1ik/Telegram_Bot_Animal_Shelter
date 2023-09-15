package com.telegram_bot_animal_shelter.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import com.telegram_bot_animal_shelter.exceptions.MenuDoesntWorkException;
import com.telegram_bot_animal_shelter.keyboard.KeyBoardShelter;
import com.telegram_bot_animal_shelter.model.Cat;
import com.telegram_bot_animal_shelter.model.Dog;
import com.telegram_bot_animal_shelter.model.PersonCat;
import com.telegram_bot_animal_shelter.model.PersonDog;
import com.telegram_bot_animal_shelter.model.Status;
import com.telegram_bot_animal_shelter.repository.PersonCatRepository;
import com.telegram_bot_animal_shelter.repository.PersonDogRepository;
import com.telegram_bot_animal_shelter.repository.ReportRepository;
import com.telegram_bot_animal_shelter.service.ReportService;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TelegramBotUpdateListener.class})
@ExtendWith(SpringExtension.class)
class TelegramBotUpdateListenerTest {
    @MockBean
    private KeyBoardShelter keyBoardShelter;

    @MockBean
    private PersonCatRepository personCatRepository;

    @MockBean
    private PersonDogRepository personDogRepository;

    @MockBean
    private ReportRepository reportRepository;

    @MockBean
    private ReportService reportService;

    @MockBean
    private TelegramBot telegramBot;

    @Autowired
    private TelegramBotUpdateListener telegramBotUpdateListener;

    private Dog dog;
    private Dog dog2;
    private PersonDog personDog;
    private PersonDog personDog2;
    private Cat cat;
    private Cat cat2;
    private PersonCat personCat;
    private PersonCat personCat2;



    @BeforeEach
    void setUp(){
        dog = new Dog();
        dog.setAge(1);
        dog.setBreed("Breed");
        dog.setDescription("Description");
        dog.setId(1L);
        dog.setName("Name");

        personDog = new PersonDog();
        personDog.setAddress("Moscow");
        personDog.setChatId(1L);
        personDog.setDog(dog);
        personDog.setId(1L);
        personDog.setName("Name");
        personDog.setPhone("+79990001122");
        personDog.setReports(new ArrayList<>());
        personDog.setStatus(Status.APPROVED);
        personDog.setYearOfBirth(1);


        dog2 = new Dog();
        dog2.setAge(1);
        dog2.setBreed("Breed");
        dog2.setDescription("Description");
        dog2.setId(2L);
        dog2.setName("Name");

        personDog2 = new PersonDog();
        personDog2.setAddress("Moscow");
        personDog2.setChatId(2L);
        personDog2.setDog(dog2);
        personDog2.setId(2L);
        personDog2.setName("Name");
        personDog2.setPhone("+79990001122");
        personDog2.setReports(new ArrayList<>());
        personDog2.setStatus(Status.REFUSED);
        personDog2.setYearOfBirth(2000);

        cat = new Cat();
        cat.setAge(1);
        cat.setBreed("Breed");
        cat.setDescription("Description");
        cat.setId(1L);
        cat.setName("Name");

        personCat = new PersonCat();
        personCat.setAddress("Moscow");
        personCat.setCat(cat);
        personCat.setChatId(1L);
        personCat.setId(1L);
        personCat.setName("Name");
        personCat.setPhone("+79990001122");
        personCat.setReports(new ArrayList<>());
        personCat.setStatus(Status.APPROVED);
        personCat.setYearOfBirth(1);

        cat2 = new Cat();
        cat2.setAge(1);
        cat2.setBreed("Breed");
        cat2.setDescription("Description");
        cat2.setId(2L);
        cat2.setName("Name");

        personCat2 = new PersonCat();
        personCat2.setAddress("Moscow");
        personCat2.setCat(cat2);
        personCat2.setChatId(2L);
        personCat2.setId(2L);
        personCat2.setName("Name");
        personCat2.setPhone("+79990001122");
        personCat2.setReports(new ArrayList<>());
        personCat2.setStatus(Status.REFUSED);
        personCat2.setYearOfBirth(2000);
    }

    @Test
    void shouldInitNotError() {
        doNothing().when(telegramBot).setUpdatesListener(Mockito.<UpdatesListener>any());
        telegramBotUpdateListener.init();
        verify(telegramBot).setUpdatesListener(Mockito.<UpdatesListener>any());
    }

    @Test
    void shouldExceptionInit() {
        doThrow(new RuntimeException("Error")).when(telegramBot).setUpdatesListener(Mockito.<UpdatesListener>any());
        assertThrows(RuntimeException.class, () -> telegramBotUpdateListener.init());
        verify(telegramBot).setUpdatesListener(Mockito.<UpdatesListener>any());
    }

    @Test
    void shouldAddUpdates() {
        ArrayList<Update> updates = new ArrayList<>();
        updates.add(new Update());
        updates.add(new Update());
        assertEquals(-1, telegramBotUpdateListener.process(updates));
    }

    @Test
    void shouldExceptionWhenAddUpdate() {
        Message message = mock(Message.class);
        when(message.text()).thenThrow(new MenuDoesntWorkException("Don't send menu"));
        when(message.chat()).thenReturn(new Chat());
        Update update = mock(Update.class);
        when(update.message()).thenReturn(message);

        ArrayList<Update> updates = new ArrayList<>();
        updates.add(update);
        assertThrows(MenuDoesntWorkException.class, () -> telegramBotUpdateListener.process(updates));
        verify(update, atLeast(1)).message();
        verify(message).chat();
        verify(message).text();
    }

    @Test
    void shouldSendReplyMessage() {
        when(telegramBot.execute(Mockito.<BaseRequest<SendMessage, SendResponse>>any())).thenReturn(null);
        telegramBotUpdateListener.sendReplyMessage(1L, "Message Text", 1);
        verify(telegramBot).execute(Mockito.<BaseRequest<SendMessage, SendResponse>>any());
    }

    @Test
    void sendReplyMessage() {
        when(telegramBot.execute(Mockito.<BaseRequest<SendMessage, SendResponse>>any()))
                .thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> telegramBotUpdateListener.sendReplyMessage(1L, "Message Text", 1));
        verify(telegramBot).execute(Mockito.<BaseRequest<SendMessage, SendResponse>>any());
    }

    @Test
    void shouldSendMessage() {
        when(telegramBot.execute(Mockito.<BaseRequest<SendMessage, SendResponse>>any())).thenReturn(null);
        telegramBotUpdateListener.sendMessage(1L, "Text");
        verify(telegramBot).execute(Mockito.<BaseRequest<SendMessage, SendResponse>>any());
    }

    @Test
    void shouldExceptionWhenSendMessage() {
        when(telegramBot.execute(Mockito.<BaseRequest<SendMessage, SendResponse>>any()))
                .thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> telegramBotUpdateListener.sendMessage(1L, "Text"));
        verify(telegramBot).execute(Mockito.<BaseRequest<SendMessage, SendResponse>>any());
    }

    @Test
    void shouldShareContact() {
        Update update = mock(Update.class);
        when(update.message()).thenReturn(new Message());
        telegramBotUpdateListener.shareContact(update);
        verify(update).message();
    }

    @Test
    void shouldShareContactAndFindAllPerson() {
        when(personDogRepository.findAll()).thenReturn(new ArrayList<>());
        when(personCatRepository.findAll()).thenReturn(new ArrayList<>());
        when(telegramBot.execute(Mockito.<BaseRequest<DeleteMyCommands, BaseResponse>>any())).thenReturn(null);
        Message message = mock(Message.class);
        when(message.messageId()).thenReturn(1);
        when(message.chat()).thenReturn(new Chat());
        when(message.contact()).thenReturn(new Contact());
        Update update = mock(Update.class);
        when(update.message()).thenReturn(message);
        telegramBotUpdateListener.shareContact(update);
        verify(personDogRepository).findAll();
        verify(personCatRepository).findAll();
        verify(telegramBot, atLeast(1)).execute(Mockito.<BaseRequest<DeleteMyCommands, BaseResponse>>any());
        verify(update, atLeast(1)).message();
        verify(message, atLeast(1)).chat();
        verify(message, atLeast(1)).contact();
        verify(message).messageId();
    }

}

