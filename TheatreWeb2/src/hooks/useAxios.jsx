import { useEffect, useReducer, useCallback } from "react";

import client from "../config/axios/client";
import { GET, DELETE } from "../config/axios/httpMethods";

const Actions = {
  INIT: "INIT",
  SET_LOADING: "SET_LOADING",
  SET_DATA: "SET_DATA",
  SET_ERROR: "SET_ERROR",
  SET_CONFIG: "SET_CONFIG",
};

const reducer = (state, action) => {
  switch (action.type) {
    case Actions.INIT:
      return {
        ...state,
        data: null,
        error: null,
        methodType: null,
        methodData: null,
        url: null,
      };
    case Actions.SET_LOADING:
      return { ...state, loading: action.payload };
    case Actions.SET_DATA:
      return { ...state, data: action.payload, error: null };
    case Actions.SET_ERROR:
      return { ...state, error: action.payload, data: null };
    case Actions.SET_CONFIG:
      return {
        ...state,
        methodType: action.payload.methodType,
        methodData: action.payload.methodData,
        url: action.payload.url,
      };
    default:
      throw new Error("Unexpected action in useAxios hook");
  }
};
const useAxios = (methodType, url, methodData) => {
  const [axiosState, dispatch] = useReducer(reducer, {
    data: null,
    error: null,
    loading: false,
    methodType,
    methodData,
    url,
  });
  const setConfigParams = useCallback((url, methodType, methodData) => {
    if (!url || !methodType)
      throw new Error("Missing parameters url and methodType !");

    dispatch({
      type: Actions.SET_CONFIG,
      payload: { url, methodType, methodData },
    });
  }, []);

  const init = useCallback(() => {
    dispatch({
      type: Actions.INIT,
    });
  }, []);

  const setUpConfig = useCallback((methodType, url, methodData, signal) => {
    let config = {
      method: methodType,
      url: url,
      signal: signal,
    };

    if ((methodType === GET || methodType === DELETE) && methodData) {
      config.params = methodData;
    } else if (methodData) {
      config.data = methodData;
    }
    return config;
  }, []);

  useEffect(() => {
    if (!axiosState.methodType || !axiosState.url) return;

    const controller = new AbortController();
    const config = setUpConfig(
      axiosState.methodType,
      axiosState.url,
      axiosState.methodData,
      controller.signal
    );

    if (config.signal.aborted) return;

    dispatch({ type: Actions.SET_LOADING, payload: true });

    client(config)
      .then((response) => {
        dispatch({ type: Actions.SET_DATA, payload: response.data });
      })
      .catch((error) => {
        if (error.name === "AbortError") return;
        dispatch({ type: Actions.SET_ERROR, payload: error });
      })
      .finally(() => {
        dispatch({ type: Actions.SET_LOADING, payload: false });
      });

    return () => {
      if (controller) {
        controller.abort();
      }
    };
  }, [
    axiosState.methodType,
    axiosState.url,
    axiosState.methodData,
    setUpConfig,
  ]);

  return [
    axiosState.data,
    axiosState.error,
    axiosState.loading,
    setConfigParams,
    init,
  ];
};

export default useAxios;
