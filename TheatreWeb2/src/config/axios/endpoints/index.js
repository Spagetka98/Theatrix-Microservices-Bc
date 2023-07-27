import { GET, POST, PUT, DELETE } from "../httpMethods";

export const GET_TOP_ACTIVITIES = {
  method: GET,
  url: "/activities/getTopActivities",
};
export const GET_ACTIVITIES_FOR_CURRENT_DAY = {
  method: GET,
  url: "/activities/getActivitiesForCurrentDay",
};
export const GET_ALL_DIVISIONS = {
  method: GET,
  url: "/activities/getDivisions",
};
export const GET_ALL_PREVIEWS_PAGE = {
  method: GET,
  url: "/activities/allPreviews",
};
export const ADD_LIKED_ACTIVITY = {
  method: POST,
  url: "/activities/activityLiked",
};
export const ADD_DISLIKED_ACITIVITY = {
  method: POST,
  url: "/activities/activityDisliked",
};
export const GET_ACTIVITY = {
  method: GET,
  url: "/activities/getActivity",
};
export const GET_ACTIVITIES_DETAILS = {
  method: GET,
  url: "/activities/getActivitiesDetails",
};

export const AUTHENTICATE_USER = {
  method: POST,
  url: "/auth/signin",
};
export const REGISTER_USER = {
  method: POST,
  url: "/auth/signup",
};
export const LOGOUT = {
  method: GET,
  url: "/auth/signout",
};

export const REFRESH_TOKEN = {
  method: POST,
  url: "/expiration/refreshToken",
};

export const GET_ACTIVITY_RATING_NUMBERS = {
  method: GET,
  url: "/rating/getActivityRatings",
};
export const ADD_RATING = {
  method: POST,
  url: "/rating/addRating",
};
export const GET_ACTIVITY_USER_RATING = {
  method: GET,
  url: "/rating/getActivityUserRating",
};
export const DELETE_ACITIVITY_RATING = {
  method: DELETE,
  url: "/rating/deleteActivityRating",
};
export const CHANGE_ACTIVITY_RATING = {
  method: PUT,
  url: "/rating/changeActivityRating",
};
export const GET_RATING_PREVIEWS = {
  method: GET,
  url: "/rating/getRatingPreviews",
};
export const GET_USER_RATING_REVIEWS = {
  method: GET,
  url: "/rating/getUserRatings",
};

export const CHANGE_PASSWORD = {
  method: PUT,
  url: "user/changePassword",
};
