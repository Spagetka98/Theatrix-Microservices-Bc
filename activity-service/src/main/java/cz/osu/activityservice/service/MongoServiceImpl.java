package cz.osu.activityservice.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import cz.osu.activityservice.error.exception.MongoDatabaseUnavailableException;
import cz.osu.activityservice.properties.MongoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoServiceImpl implements MongoService{
    private final MongoClient mongoClient;
    private final MongoProperties mongoProperties;

    /**
     * @see MongoServiceImpl#testDatabaseConnection()
     */
    @Override
    public void testDatabaseConnection() throws MongoDatabaseUnavailableException {
        try {
            mongoClient.getDatabase(mongoProperties.name()).runCommand(new BasicDBObject("ping", "1"));
        } catch (Exception exp) {
            throw new MongoDatabaseUnavailableException(String.format("Mongo Database: %s is not responding !", mongoProperties.name()));
        }
    }
}
