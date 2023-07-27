package cz.osu.activityservice.model.message;

public record RatingActionMessage(String userID, long idActivity,double ratingValue,boolean isUpdate,String timeOfSend) {}
