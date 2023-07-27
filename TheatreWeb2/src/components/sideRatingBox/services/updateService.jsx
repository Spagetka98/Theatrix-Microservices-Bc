import { useState, useEffect } from "react";

import useAxios from "../../../hooks/useAxios";

import { titleValidation, textValidation } from "../ratingFormValidation";

import { COMP_SIDE_RATING_BOX_SERVICE_UPDATE } from "../../../config/texts";
import { CHANGE_ACTIVITY_RATING } from "../../../config/axios/endpoints";

import Actions from "../formActions";

const useUpdateService = (
  id,
  dispatch,
  titleRef,
  textAreaRef,
  ratingRef,
  previousRatingDataRef,
  realoadHandler
) => {
  const [userRating, setUserRating] = useState(null);
  const [data, error, loading, setConfigParams, init] = useAxios();

  useEffect(() => {
    if (data?.length === 0 && userRating) {
      dispatch({
        type: Actions.SET_MESSAGE,
        payload: {
          isError: false,
          message: COMP_SIDE_RATING_BOX_SERVICE_UPDATE.updated,
        },
      });
      dispatch({
        type: Actions.SET_USER_RATING,
        payload: userRating,
      });
      realoadHandler();
    }
  }, [data, dispatch, userRating, realoadHandler]);

  useEffect(() => {
    if (error) {
      dispatch({
        type: Actions.SET_MESSAGE,
        payload: {
          isError: true,
          message: COMP_SIDE_RATING_BOX_SERVICE_UPDATE.error,
        },
      });
    }
  }, [error, dispatch]);

  useEffect(() => {
    if (loading) {
      dispatch({
        type: Actions.CLEAR_MESSAGE,
      });
      dispatch({ type: Actions.SET_LOADING, payload: true });
    } else {
      dispatch({ type: Actions.SET_LOADING, payload: false });
    }
  }, [loading, dispatch]);

  const updateHandler = (e) => {
    e.preventDefault();

    const title = titleRef.current.value?.trim().replace(/\s/g, " ");
    const errorTitle = titleValidation(title);
    if (errorTitle) {
      dispatch({
        type: Actions.SET_MESSAGE,
        payload: { isError: true, message: errorTitle },
      });
      return;
    }

    const text = textAreaRef.current.value?.trim().replace(/\s/g, " ");
    const errorText = textValidation(text);
    if (errorText) {
      dispatch({
        type: Actions.SET_MESSAGE,
        payload: { isError: true, message: errorText },
      });
      return;
    }

    const rating = ratingRef.current.getRating();

    const currentUserRatingData = {
      userRating: rating,
      ratingTitle: title,
      ratingText: text,
    };

    if (
      JSON.stringify(previousRatingDataRef.current) !==
      JSON.stringify(currentUserRatingData)
    ) {
      init();
      previousRatingDataRef.current = { ...currentUserRatingData };

      setUserRating(currentUserRatingData);
      setConfigParams(
        CHANGE_ACTIVITY_RATING.url,
        CHANGE_ACTIVITY_RATING.method,
        {
          idActivity: id,
          rating: rating,
          title: title,
          text: text,
        }
      );
    } else {
      dispatch({
        type: Actions.SET_MESSAGE,
        payload: {
          isError: true,
          message: COMP_SIDE_RATING_BOX_SERVICE_UPDATE.updateError,
        },
      });
    }
  };

  return updateHandler;
};

export default useUpdateService;
