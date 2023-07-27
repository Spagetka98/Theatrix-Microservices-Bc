import { USER, LOGGED_DATE } from "../../config/localStorage/items";
import {
  getItemLocalStorage,
  removeItemLocalStorage,
} from "../../config/localStorage/services";

const REFRESH_TOKEN_EXPIRATION_MINUTES = 30;

const getUser = () => {
  const logged_date = new Date(getItemLocalStorage(LOGGED_DATE));
  let current_date = new Date();

  logged_date.setMinutes(
    logged_date.getMinutes() + REFRESH_TOKEN_EXPIRATION_MINUTES
  );

  if (current_date <= logged_date) return getItemLocalStorage(USER);

  removeItemLocalStorage(USER);
  removeItemLocalStorage(LOGGED_DATE);
  return null;
};

export default getUser;
