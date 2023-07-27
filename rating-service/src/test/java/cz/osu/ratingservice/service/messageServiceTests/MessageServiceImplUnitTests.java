package cz.osu.ratingservice.service.messageServiceTests;

import cz.osu.ratingservice.service.MessageServiceImpl;
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

    @DisplayName("sendRatingActionNotice - Should throw IllegalArgumentException because the inserted userID is empty string")
    @Test
    void sendRatingActionNoticeExceptionCheck() {
        assertThatThrownBy(() ->
                messageService.sendRatingActionNotice("", 1, 1,false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("requeueAvailabilityMessage - Should throw IllegalArgumentException because the inserted theatreActivityAvailabilityMessage is null")
    @Test
    void requeueAvailabilityMessageExceptionCheck() {
        assertThatThrownBy(() ->
                messageService.requeueAvailabilityMessage(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("sendFailedActivityMessage - Should throw IllegalArgumentException because the inserted theatreActivityAvailabilityMessage is null")
    @Test
    void sendFailedActivityMessageExceptionCheck() {
        assertThatThrownBy(() ->
                messageService.sendFailedActivityMessage(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
