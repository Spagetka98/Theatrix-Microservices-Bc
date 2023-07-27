import { useState, useEffect } from "react";

import useAxios from "../../../hooks/useAxios";

import { titleValidation, textValidation } from "../ratingFormValidation";
import Actions from "../formActions";

import { COMP_SIDE_RATING_BOX_SERVICE_ADD } from "../../../config/texts";
import { ADD_RATING } from "../../../config/axios/endpoints";

const useAddService = (
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
          message: COMP_SIDE_RATING_BOX_SERVICE_ADD.added,
        },
      });
      dispatch({
        type: Actions.SET_USER_RATED,
        payload: true,
      });
      dispatch({
        type: Actions.SET_USER_RATING,
        payload: {
          userRating: userRating.rating,
          ratingTitle: userRating.title,
          ratingText: userRating.text,
        },
      });
      previousRatingDataRef.current = {
        userRating: userRating.rating,
        ratingTitle: userRating.title,
        ratingText: userRating.text,
      };
      realoadHandler();
    }
  }, [data, dispatch, userRating, previousRatingDataRef, realoadHandler]);

  useEffect(() => {
    if (error) {
      dispatch({
        type: Actions.SET_MESSAGE,
        payload: {
          isError: true,
          message: COMP_SIDE_RATING_BOX_SERVICE_ADD.error,
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

  const addHandler = (e) => {
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
    const ratingData = {
      idActivity: id,
      rating: rating,
      title: title,
      text: text,
    };
    init();
    setUserRating(ratingData);
    setConfigParams(ADD_RATING.url, ADD_RATING.method, ratingData);
  };

  return addHandler;
};

export default useAddService;
