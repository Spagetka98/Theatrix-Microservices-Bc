import { useEffect } from "react";

import useAxios from "../../../hooks/useAxios";

import { COMP_SIDE_RATING_BOX_SERVICE_REMOVE } from "../../../config/texts";
import { DELETE_ACITIVITY_RATING } from "../../../config/axios/endpoints";

import Actions from "../formActions";

const useRemoveService = (id, dispatch, realoadHandler) => {
  const [data, error, loading, setConfigParams, init] = useAxios();

  useEffect(() => {
    if (data?.length === 0) {
      dispatch({
        type: Actions.SET_MESSAGE,
        payload: {
          isError: false,
          message: COMP_SIDE_RATING_BOX_SERVICE_REMOVE.removed,
        },
      });
      dispatch({
        type: Actions.DELETE,
      });
      realoadHandler();
    }
  }, [data, dispatch, realoadHandler]);

  useEffect(() => {
    if (error) {
      dispatch({
        type: Actions.SET_MESSAGE,
        payload: {
          isError: true,
          message: COMP_SIDE_RATING_BOX_SERVICE_REMOVE.error,
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

  const removeHandler = (e) => {
    e.preventDefault();
    init();
    setConfigParams(
      DELETE_ACITIVITY_RATING.url,
      DELETE_ACITIVITY_RATING.method,
      { id: id }
    );
  };

  return removeHandler;
};

export default useRemoveService;
