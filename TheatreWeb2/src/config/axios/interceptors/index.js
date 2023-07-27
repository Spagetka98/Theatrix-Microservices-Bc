import axiosInstance from "../client";

import { REFRESH_TOKEN_CON } from "../../cookies/items";
import { getItemCookies } from "../../cookies/services";

import { logout, updateAccessToken } from "../../../state/slices/user";
import { setMessage } from "../../../state/slices/message";
import { clearAllActivitiesFiltters } from "../../../state/slices/allActivities";
import { clearLikedActivitiesFiltters } from "../../../state/slices/likedActivities";
import { clearDislikedActivitiesFiltters } from "../../../state/slices/dislikedActivities";
import { clearRatedActivitiesFiltters } from "../../../state/slices/ratedActivities";

import { REFRESH_TOKEN } from "../endpoints";
import UnauthorizedEndpoints from "./unauthorizedEndpoints";

import { CONFIG_INTERCEPTORS_RESPONSE } from "../../texts";

const interceptorSetup = (store) => {
  const { dispatch } = store;

  axiosInstance.interceptors.request.use(
    (config) => {
      const accessToken = store?.getState()?.user?.value?.user?.accessToken;

      if (accessToken) {
        config.headers["Authorization"] = "Bearer " + accessToken;
      }

      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  axiosInstance.interceptors.response.use(
    (respon) => {
      return respon;
    },
    async (error) => {
      try {
        const config = error.config;
        const status = error.response.status;

        if (!UnauthorizedEndpoints.includes(config.url) && error.response) {
          if (status === 401 && !config.accessTokenExpired) {
            config.accessTokenExpired = true;

            const response = await axiosInstance.post(REFRESH_TOKEN.url, {
              refreshToken: getItemCookies(REFRESH_TOKEN_CON),
            });

            const { accessToken } = response.data;

            dispatch(updateAccessToken(accessToken));

            return axiosInstance(config);
          } else if (status === 403 && !config.refreshTokenExpired) {
            config.refreshTokenExpired = true;

            dispatch(logout());
            dispatch(
              setMessage({
                isError: true,
                msg: CONFIG_INTERCEPTORS_RESPONSE.refreshTokenExpired,
              })
            );
            dispatch(clearAllActivitiesFiltters());
            dispatch(clearLikedActivitiesFiltters());
            dispatch(clearDislikedActivitiesFiltters());
            dispatch(clearRatedActivitiesFiltters());
          }
        }
        return Promise.reject(error);
      } catch (err) {
        return Promise.reject(err);
      }
    }
  );
};

export default interceptorSetup;
