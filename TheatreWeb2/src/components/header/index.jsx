import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";
import tw from "twin.macro";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import Snackbar from "@mui/material/Snackbar";

import useAxios from "../../hooks/useAxios";

import MenuIcon from "../../assets/menu";
import XIcon from "../../assets/x";

import NavigationLinks from "../../config/navigation/links";
import { HOME, LOGIN, REGISTRATION } from "../../config/navigation/paths";
import { LOGOUT } from "../../config/axios/endpoints";
import { COMP_HEADER } from "../../config/texts";

import { logout } from "../../state/slices/user";
import { clearAllActivitiesFiltters } from "../../state/slices/allActivities";
import { clearLikedActivitiesFiltters } from "../../state/slices/likedActivities";
import { clearDislikedActivitiesFiltters } from "../../state/slices/dislikedActivities";
import { clearRatedActivitiesFiltters } from "../../state/slices/ratedActivities";

import DropMenu from "./dropMenu";
import useMainMenu from "./service";

const Header = () => {
  const {
    toggleMobileDropdownMenu,
    toggleMobileMenuOff,
    isOpenMobileMenu,
    isMobile,
    menuRef,
  } = useMainMenu();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [data, error, loading, setConfigParams, init] = useAxios();
  const [snackBar, setSnackBar] = useState(false);
  const [message, setMessage] = useState(null);
  const user = useSelector((state) => state?.user?.value?.isLogged);

  const logoutHandler = () => {
    setConfigParams(LOGOUT.url, LOGOUT.method);
  };

  useEffect(() => {
    if (loading) {
      setSnackBar(true);
      setMessage({ success: null, msg: COMP_HEADER.signingOut });
    }
  }, [loading]);

  useEffect(() => {
    if (error) {
      setMessage({ success: false, msg: COMP_HEADER.error });
    }
  }, [error]);

  useEffect(() => {
    if (data === "") {
      dispatch(logout());
      dispatch(clearAllActivitiesFiltters());
      dispatch(clearLikedActivitiesFiltters());
      dispatch(clearDislikedActivitiesFiltters());
      dispatch(clearRatedActivitiesFiltters());
      navigate(HOME);
      init();
      setMessage({ success: true, msg: COMP_HEADER.signOut });
    }
  }, [navigate, dispatch, data, init]);

  return (
    <>
      <NavigationHeader>
        <Logo to={HOME}>{COMP_HEADER.title}</Logo>
        <div ref={menuRef}>
          <HamburgerMenu onClick={isMobile ? toggleMobileDropdownMenu : null}>
            {isOpenMobileMenu ? <XIcon /> : <MenuIcon />}
          </HamburgerMenu>

          <NavigationBar isOpenMobileMenu={isOpenMobileMenu}>
            {user ? (
              <>
                <DropMenu
                  menuData={NavigationLinks.ActivitiesLinks}
                  isMobile={isMobile}
                  isOpenMobileMenu={isOpenMobileMenu}
                  toggleMobileMenuOff={toggleMobileMenuOff}
                />
                <DropMenu
                  menuData={NavigationLinks.SettingLinks}
                  isMobile={isMobile}
                  isOpenMobileMenu={isOpenMobileMenu}
                  toggleMobileMenuOff={toggleMobileMenuOff}
                />
                <NavLink to={HOME}>
                  <LogoutBtn disabled={loading} onClick={logoutHandler}>
                    {COMP_HEADER.logout}
                  </LogoutBtn>
                </NavLink>
              </>
            ) : (
              <>
                <NavLink to={LOGIN}>
                  <LoginBtn disabled={loading}>{COMP_HEADER.login}</LoginBtn>
                </NavLink>
                <NavLink to={REGISTRATION}>
                  <RegistrateBtn disabled={loading}>
                    {COMP_HEADER.registrate}
                  </RegistrateBtn>
                </NavLink>
              </>
            )}
          </NavigationBar>
        </div>
      </NavigationHeader>

      <Snackbar
        open={snackBar}
        autoHideDuration={loading ? 99999999 : 2000}
        onClose={() => setSnackBar(false)}
      >
        <SnackBody $type={message?.success}>{message?.msg}</SnackBody>
      </Snackbar>
    </>
  );
};

const NavigationHeader = styled.header`
  ${tw`w-full relative flex flex-row justify-between items-center px-2.5 py-4  md:py-0 shadow-md z-50 bg-gradient-to-r from-[#FFAFBD] to-[#F4E2D8]`}
`;

const Logo = styled(Link)`
  ${tw`text-2xl md:text-3xl xl:text-4xl 3xl:text-6xl 4xl:text-7xl italic font-extralight transition-all`}
`;

const HamburgerMenu = styled.div`
  ${tw`md:hidden uppercase `}
`;

const NavigationBar = styled.nav(({ isOpenMobileMenu }) => [
  tw`hidden border-2 w-full absolute top-full left-0 z-50  flex-col bg-white
                md:(static flex flex-row justify-end bg-transparent border-0)`,
  isOpenMobileMenu && tw`flex`,
]);

const Button = styled.button`
  ${tw`text-white font-light md:hover:animate-pulse text-center py-2 px-4 3xl:py-8 3xl:px-16 text-sm sm:text-lg 2xl:text-xl 3xl:text-2xl 4xl:text-3xl rounded-lg`}
`;

const LogoutBtn = styled(Button)`
  ${tw`bg-red-500`}
`;

const LoginBtn = styled(Button)`
  ${tw`bg-green-500`}
`;

const RegistrateBtn = styled(Button).attrs({
  className: "bg-orange-500",
})``;

const NavLink = styled(Link)`
  ${tw`p-2.5 text-center`}
`;

const SnackBody = styled.div(({ $type }) => [
  tw`animate-bounce bg-yellow-400 p-4 rounded-xl text-white shadow-xl font-extrabold text-base lg:text-lg xl:text-xl 2xl:text-2xl 3xl:text-3xl 4xl:text-5xl transition-all text-center`,
  $type === true && tw`bg-green-500`,
  $type === false && tw`bg-red-500`,
]);
export default Header;
