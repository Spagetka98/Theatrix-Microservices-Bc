package cz.osu.ratingservice.service.mongoServiceTests;

import com.mongodb.client.MongoClient;
import cz.osu.ratingservice.error.exception.MongoDatabaseUnavailableException;
import cz.osu.ratingservice.properties.MongoProperties;
import cz.osu.ratingservice.service.MongoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MongoServiceImplUnitTests {
    @Mock
    private MongoClient mongoClient;
    @Mock
    private MongoProperties mongoProperties;
    @InjectMocks
    private MongoServiceImpl mongoService;

    @DisplayName("testDatabaseConnection - Should throw an MongoDatabaseUnavailableException whenever the database is not responding or the connection data are incorrect")
    @Test
    void testDatabaseConnectionExceptionCheck() {
        String dbName = "testDB";
        when(mongoProperties.name())
                .thenReturn(dbName);
        when(mongoClient.getDatabase(dbName))
                .thenThrow(RuntimeException.class);

        assertThatThrownBy(() ->
                this.mongoService.testDatabaseConnection())
                .isInstanceOf(MongoDatabaseUnavailableException.class);
    }
}
