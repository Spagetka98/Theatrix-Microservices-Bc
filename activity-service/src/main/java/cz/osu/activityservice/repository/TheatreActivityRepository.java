package cz.osu.activityservice.repository;

import cz.osu.activityservice.model.database.TheatreActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TheatreActivityRepository extends MongoRepository<TheatreActivity, String> {
    Optional<TheatreActivity> findByIdActivity(long idActivity);

    List<TheatreActivity> findAllByIdActivityIn(List<Long> ids);

    List<TheatreActivity> findAllByStartDateBetweenOrderByStartDateAsc(Instant startDate, Instant endDate);

    @Aggregation(pipeline = {
            "{$addFields: { 'ratings_count': {$size: { $ifNull: [ $ratedByUsers, [] ] } } }}",
            "{ $sort: {'ratings_count':-1}}",
            " { $limit : 3 }",
    })
    List<TheatreActivity> findTop3Activities();

    @Aggregation(pipeline = {"{ '$group': { '_id' : '$division' } }"})
    List<String> findDistinctDivision();

    @Query(value = "{ 'name':{$regex:?0,$options:'i'},'$or':[ {'schemalessData.author':{$regex:?1,$options:'i'}}, {'schemalessData.author':{ $exists : ?2 }} ], 'startDate': {$gte:?3,$lte:?4},'division': {$in : ?5 }}",
            sort = "{'startDate':1}")
    Page<TheatreActivity> searchTheatreActivities(String name, String author,boolean isAuthorSearched, Instant from, Instant to, List<String> division, Pageable pageable);

    @Query(value = "{ 'name':{$regex:?0,$options:'i'},'$or':[ {'schemalessData.author':{$regex:?1,$options:'i'}}, {'schemalessData.author':{ $exists : ?2 }} ], 'startDate': {$gte:?3,$lte:?4},'division': {$in : ?5 },'likedByUsers': {$in : [?6] } }",
            sort = "{'startDate':1}")
    Page<TheatreActivity> searchLikedTheatreActivities(String name, String author,boolean isAuthorSearched, Instant from, Instant to, List<String> division, String userId, Pageable pageable);

    @Query(value = "{ 'name':{$regex:?0,$options:'i'},'$or':[ {'schemalessData.author':{$regex:?1,$options:'i'}}, {'schemalessData.author':{ $exists : ?2 }} ], 'startDate': {$gte:?3,$lte:?4},'division': {$in : ?5 },'dislikedByUsers': {$in : [?6] } }",
            sort = "{'startDate':1}")
    Page<TheatreActivity> searchDislikedTheatreActivities(String name, String author,boolean isAuthorSearched, Instant from, Instant to, List<String> division, String userId, Pageable pageable);

    @Query(value = "{ 'name':{$regex:?0,$options:'i'},'$or':[ {'schemalessData.author':{$regex:?1,$options:'i'}}, {'schemalessData.author':{ $exists : ?2 }} ], 'startDate': {$gte:?3,$lte:?4},'division': {$in : ?5 },'ratedByUsers': {$in : [?6] }}",
            sort = "{'startDate':1}")
    Page<TheatreActivity> searchRatedTheatreActivities(String name, String author,boolean isAuthorSearched, Instant from, Instant to, List<String> division, String userId, Pageable pageable);

    @Query(value = "{ 'startDate': {$gte:?0,$lte:?1} }")
    List<TheatreActivity> searchDateBetween(Instant from, Instant to);

}
