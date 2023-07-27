import { useState, useEffect, useRef, useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import { login } from "../../state/slices/user";

import { ALL_ACTIVITIES } from "../../config/navigation/paths";
import { AUTHENTICATE_USER } from "../../config/axios/endpoints";

import useAxios from "../../hooks/useAxios";

import { PAGES_LOGIN_SERVICE } from "../../config/texts";

import { deleteMessage } from "../../state/slices/message";

const useLogin = () => {
  const usernameRef = useRef(null);
  const passwordRef = useRef(null);
  const [loginError, setLoginError] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const message = useSelector((state) => state?.msg?.value?.msg);
  const isLogged = useSelector((state) => state?.user?.value?.isLogged);
  const [data, error, loading, setConfigParams] = useAxios();

  const loginHandler = (e) => {
    e.preventDefault();
    setLoginError(null);
    dispatch(deleteMessage());

    const username = usernameRef.current.getInputData();
    const password = passwordRef.current.getInputData();

    if (username && password) {
      setConfigParams(AUTHENTICATE_USER.url, AUTHENTICATE_USER.method, {
        username,
        password,
      });
    }
  };

  useEffect(() => {
    if (data?.accessToken) {
      usernameRef.current.clearData();
      passwordRef.current.clearData();
      dispatch(login(data));

      navigate(ALL_ACTIVITIES);
    }
  }, [data, dispatch, navigate]);

  useEffect(() => {
    if (error === null || error === undefined) {
      setLoginError(null);
    } else if (error?.response?.status === 400) {
      setLoginError({
        isError: true,
        msg: PAGES_LOGIN_SERVICE.credentialsError,
      });
    } else {
      setLoginError({
        isError: true,
        msg: PAGES_LOGIN_SERVICE.serverError,
      });
    }
  }, [error]);

  useEffect(() => {
    if (message) {
      setLoginError({
        isError: true,
        msg: message,
      });
    }
    return () => {
      dispatch(deleteMessage());
    };
  }, [message, dispatch]);

  useEffect(() => {
    if (isLogged) {
      navigate(ALL_ACTIVITIES);
    }
  }, [isLogged, navigate]);

  const emptyValidation = useCallback((value) => {
    if (!value) {
      return PAGES_LOGIN_SERVICE.emptyInput;
    }
  }, []);

  return {
    usernameRef,
    passwordRef,
    loginError,
    loading,
    loginHandler,
    emptyValidation,
  };
};

export default useLogin;
