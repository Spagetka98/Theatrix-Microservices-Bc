import React from "react";
import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";
import { LOGIN } from "../../config/navigation/paths";

const withAuth = (Component) => {
  const AuthRoute = (props) => {
    const isLogged = useSelector((state) => state?.user?.value?.isLogged);

    if (isLogged) {
      return <Component {...props} />;
    } else {
      return <Navigate replace to={LOGIN} />;
    }
  };
  return AuthRoute;
};
export default withAuth;
