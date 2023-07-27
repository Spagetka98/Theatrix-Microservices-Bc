package cz.osu.ratingservice.service;

import cz.osu.ratingservice.error.exception.MongoDatabaseUnavailableException;

public interface MongoService {
    /**
     * Run a ping command to the mongoDB and check if it is available.
     * If not throw an exception.
     *
     * @throws MongoDatabaseUnavailableException if mongoDB is not responding
     */
    void testDatabaseConnection() throws MongoDatabaseUnavailableException;
}
