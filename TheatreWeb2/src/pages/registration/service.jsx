import { useState, useRef, useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import { REGISTER_USER } from "../../config/axios/endpoints";
import { PAGES_REGISTRATION_SERVICE } from "../../config/texts";
import { ALL_ACTIVITIES } from "../../config/navigation/paths";

import useAxios from "../../hooks/useAxios";

const useRegistration = () => {
  const usernameBoxRef = useRef(null);
  const emailBoxRef = useRef(null);
  const passwordBoxRef = useRef(null);
  const [message, setMessage] = useState(null);
  const navigate = useNavigate();
  const isLogged = useSelector((state) => state?.user?.value?.isLogged);

  const [data, error, loading, setConfigParams] = useAxios();

  const registrationHandler = (e) => {
    e.preventDefault();
    setMessage(null);

    const username = usernameBoxRef.current.getInputData();
    const email = emailBoxRef.current.getInputData();
    const password = passwordBoxRef.current.getInputData();

    if (username && email && password) {
      setConfigParams(REGISTER_USER.url, REGISTER_USER.method, {
        username,
        email,
        password,
      });
    }
  };

  useEffect(() => {
    if (data === "") {
      setMessage({
        isError: false,
        msg: PAGES_REGISTRATION_SERVICE.registrationSuccess,
      });

      usernameBoxRef.current.clearData();
      emailBoxRef.current.clearData();
      passwordBoxRef.current.clearData();
    }
  }, [data]);

  useEffect(() => {
    if (error === undefined || error === null) return;
    const errorStatus = error?.response?.status;
    const errorMessage = error?.response?.data?.message;

    if (errorStatus === 400 && errorMessage === "NAME_TAKEN") {
      setMessage({
        isError: true,
        msg: PAGES_REGISTRATION_SERVICE.nameTaken,
      });
    } else if (errorStatus === 400 && errorMessage === "EMAIL_TAKEN") {
      setMessage({
        isError: true,
        msg: PAGES_REGISTRATION_SERVICE.emailTaken,
      });
    } else {
      setMessage({
        isError: true,
        msg: PAGES_REGISTRATION_SERVICE.serverNotRespond,
      });
    }
  }, [error]);

  useEffect(() => {
    if (isLogged) {
      navigate(ALL_ACTIVITIES);
    }
  }, [isLogged, navigate]);

  return {
    usernameBoxRef,
    emailBoxRef,
    passwordBoxRef,
    loading,
    message,
    registrationHandler,
  };
};

export default useRegistration;
