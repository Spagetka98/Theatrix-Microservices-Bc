import { createSlice } from "@reduxjs/toolkit";

import { USER, LOGGED_DATE } from "../../config/localStorage/items";
import {
  setItemLocalStorage,
  removeItemLocalStorage,
} from "../../config/localStorage/services";
import getUser from "../../helpersFunc/userLoginCheck";
import { REFRESH_TOKEN_CON } from "../../config/cookies/items";
import {
  setItemCookies,
  removeItemCookies,
} from "../../config/cookies/services";

const NAME_SLICE = "user";
const user_data = getUser();

const initialStateValue = user_data
  ? { isLogged: true, user: user_data }
  : { isLogged: false, user: null };

export const userSlice = createSlice({
  name: NAME_SLICE,
  initialState: { value: initialStateValue },
  reducers: {
    login: (state, action) => {
      let userData = action.payload;

      setItemCookies(REFRESH_TOKEN_CON, userData.refreshToken);
      delete userData.refreshToken;

      state.value.user = { ...userData };

      delete userData.accessToken;

      setItemLocalStorage(USER, userData);
      setItemLocalStorage(LOGGED_DATE, new Date());

      state.value.isLogged = true;
    },
    logout: (state) => {
      removeItemLocalStorage(USER);
      removeItemLocalStorage(LOGGED_DATE);
      removeItemCookies(REFRESH_TOKEN_CON);

      state.value.user = null;
      state.value.isLogged = false;
    },
    updateAccessToken: (state, action) => {
      state.value.user.accessToken = action.payload;
    },
  },
});

export const { login, logout, updateAccessToken } = userSlice.actions;

export default userSlice.reducer;
