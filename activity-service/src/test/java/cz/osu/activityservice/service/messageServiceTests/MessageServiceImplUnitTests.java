package cz.osu.activityservice.service.messageServiceTests;

import cz.osu.activityservice.service.MessageServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplUnitTests {

    @InjectMocks
    private MessageServiceImpl messageService;

    @DisplayName("sendNewTheatreActivity - Should throw IllegalArgumentException because the inserted theatreActivity is null")
    @Test
    void sendNewTheatreActivityExceptionCheck() {
        assertThatThrownBy(() ->
                messageService.sendNewTheatreActivity(null,false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("requeueActionMessage - Should throw IllegalArgumentException because the inserted actionMessage is null")
    @Test
    void requeueActionMessageExceptionCheck() {
        assertThatThrownBy(() ->
                messageService.requeueActionMessage(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("sendFailedActionMessage - Should throw IllegalArgumentException because the inserted actionMessage is null")
    @Test
    void sendFailedActionMessageExceptionCheck() {
        assertThatThrownBy(() ->
                messageService.sendFailedActionMessage(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
