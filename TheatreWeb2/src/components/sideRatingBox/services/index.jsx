import { useEffect, useRef } from "react";

import useAddService from "./addService";
import useUpdateService from "./updateService";
import useRemoveService from "./removeService";

import useAxios from "../../../hooks/useAxios";

import { COMP_SIDE_RATING_BOX_SERVICE } from "../../../config/texts";
import { GET_ACTIVITY_USER_RATING } from "../../../config/axios/endpoints";

import Actions from "../formActions";

const useRatingForm = (id, dispatch, realoadHandler) => {
  const previousRatingDataRef = useRef(null);
  const ratingRef = useRef(null);
  const titleRef = useRef(null);
  const textAreaRef = useRef(null);

  const addHandler = useAddService(
    id,
    dispatch,
    titleRef,
    textAreaRef,
    ratingRef,
    previousRatingDataRef,
    realoadHandler
  );
  const updateHandler = useUpdateService(
    id,
    dispatch,
    titleRef,
    textAreaRef,
    ratingRef,
    previousRatingDataRef,
    realoadHandler
  );
  const removeHandler = useRemoveService(id, dispatch, realoadHandler);

  const [data, error, loading, setConfigParams] = useAxios();

  useEffect(() => {
    if (id) {
      setConfigParams(
        GET_ACTIVITY_USER_RATING.url,
        GET_ACTIVITY_USER_RATING.method,
        { id: id }
      );
    }
  }, [id, setConfigParams]);

  useEffect(() => {
    if (data) {
      dispatch({ type: Actions.INIT, payload: data });

      previousRatingDataRef.current = {
        userRating: data.userRating,
        ratingTitle: data.ratingTitle,
        ratingText: data.ratingText,
      };
    }
  }, [data, dispatch]);

  useEffect(() => {
    if (error) {
      dispatch({
        type: Actions.SET_MESSAGE,
        payload: {
          isError: true,
          message: COMP_SIDE_RATING_BOX_SERVICE.error,
        },
      });
    } else {
      dispatch({
        type: Actions.CLEAR_MESSAGE,
      });
    }
  }, [error, dispatch]);

  useEffect(() => {
    if (loading) {
      dispatch({ type: Actions.SET_LOADING, payload: true });
    } else {
      dispatch({ type: Actions.SET_LOADING, payload: false });
    }
  }, [loading, dispatch]);

  return {
    ratingRef,
    titleRef,
    textAreaRef,
    addHandler,
    updateHandler,
    removeHandler,
  };
};

export default useRatingForm;
