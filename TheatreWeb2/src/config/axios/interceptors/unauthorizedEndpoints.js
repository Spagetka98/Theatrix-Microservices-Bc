import {
  GET_TOP_ACTIVITIES,
  GET_ACTIVITIES_FOR_CURRENT_DAY,
  AUTHENTICATE_USER,
  REGISTER_USER,
} from "../endpoints";

const UnauthorizedEndpoints = [
  GET_TOP_ACTIVITIES.url,
  GET_ACTIVITIES_FOR_CURRENT_DAY.url,
  AUTHENTICATE_USER.url,
  REGISTER_USER.url,
];

export default UnauthorizedEndpoints;
