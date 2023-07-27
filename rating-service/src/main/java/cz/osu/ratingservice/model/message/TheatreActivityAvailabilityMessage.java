package cz.osu.ratingservice.model.message;

public record TheatreActivityAvailabilityMessage(long idActivity,String availabilityAt,boolean isErasingMessage){}
