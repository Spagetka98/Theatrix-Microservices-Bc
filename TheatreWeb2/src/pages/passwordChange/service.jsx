import { useState, useRef, useEffect, useCallback } from "react";

import { PAGES_PASSWORD_CHANGE_SERVICE } from "../../config/texts";
import { CHANGE_PASSWORD } from "../../config/axios/endpoints";

import useAxios from "../../hooks/useAxios";

const usePasswordChangeService = () => {
  const formInputRef = useRef(null);
  const passwordsBoxesRef = useRef(null);
  const [message, setMessage] = useState(null);
  const [data, error, loading, setConfigParams] = useAxios();

  const changeHandler = (e) => {
    e.preventDefault();

    const currentPassword = formInputRef.current.getInputData();
    const newPassword = passwordsBoxesRef.current.getInputData();
    if (currentPassword === newPassword) return;

    if (currentPassword && newPassword) {
      setConfigParams(CHANGE_PASSWORD.url, CHANGE_PASSWORD.method, {
        currentPassword,
        newPassword,
      });
    }
  };

  useEffect(() => {
    if (data === "") {
      setMessage({
        isError: false,
        msg: PAGES_PASSWORD_CHANGE_SERVICE.passwordChanged,
      });

      formInputRef.current.clearData();
      passwordsBoxesRef.current.clearData();
    }
  }, [data]);

  useEffect(() => {
    const response = error?.response;
    if (!error) return;

    if (response.data?.message === "INCORRECT_PASSWORD") {
      setMessage({
        isError: true,
        msg: PAGES_PASSWORD_CHANGE_SERVICE.wrongPasswordInput,
      });
    } else {
      setMessage({
        isError: true,
        msg: PAGES_PASSWORD_CHANGE_SERVICE.serverError,
      });
    }
  }, [error]);

  useEffect(() => {
    if (loading) {
      setMessage(null);
    }
  }, [loading]);

  const emptyValidation = useCallback((value) => {
    if (!value) {
      return PAGES_PASSWORD_CHANGE_SERVICE.emptyInput;
    }
  }, []);

  return {
    formInputRef,
    passwordsBoxesRef,
    message,
    changeHandler,
    emptyValidation,
    loading,
  };
};

export default usePasswordChangeService;
