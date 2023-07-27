import { useState, useEffect, useRef } from "react";
import { useMediaQuery } from "react-responsive";

import { MOBILE } from "../../config/sizes";

import useOutsideClick from "../../hooks/useOutsideClick";

const useMainMenu = () => {
  const [isOpenMobileMenu, setOpenMobileMenu] = useState(false);
  const isMobile = useMediaQuery({
    query: MOBILE,
  });
  const menuRef = useRef(null);

  const outsideClick = useOutsideClick(menuRef);

  const toggleMobileDropdownMenu = () => {
    setOpenMobileMenu(!isOpenMobileMenu);
  };

  const toggleMobileMenuOff = () => {
    setOpenMobileMenu(false);
  };

  useEffect(() => {
    if ((isOpenMobileMenu && !isMobile) || outsideClick) {
      setOpenMobileMenu(false);
    }
  }, [isMobile, isOpenMobileMenu, outsideClick]);

  return {
    toggleMobileDropdownMenu,
    toggleMobileMenuOff,
    isOpenMobileMenu,
    isMobile,
    menuRef,
  };
};

export default useMainMenu;
