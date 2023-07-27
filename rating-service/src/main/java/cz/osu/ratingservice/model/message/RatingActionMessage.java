package cz.osu.ratingservice.model.message;

public record RatingActionMessage(String userID, long idActivity,boolean isUpdate,double ratingValue,String timeOfSend) {}
