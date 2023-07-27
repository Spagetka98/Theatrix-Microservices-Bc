import { configureStore } from "@reduxjs/toolkit";
import userReducer from "../slices/user";
import messageReducer from "../slices/message";
import allActivitiesReducer from "../slices/allActivities";
import likedActivitiesReducer from "../slices/likedActivities";
import dislikedActivitiesReducer from "../slices/dislikedActivities";
import ratedActivitiesReducer from "../slices/ratedActivities";

const store = configureStore({
  reducer: {
    user: userReducer,
    msg: messageReducer,
    allActivities: allActivitiesReducer,
    likedActivities: likedActivitiesReducer,
    dislikedActivities: dislikedActivitiesReducer,
    ratedActivities: ratedActivitiesReducer,
  },
});

export default store;
