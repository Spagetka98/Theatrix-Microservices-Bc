import React, { useState, useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";
import tw from "twin.macro";

import DropDownMenu from "../dropdownMenu";

import useOutsideClick from "../../hooks/useOutsideClick";

const DropMenu = ({
  menuData,
  isMobile,
  isOpenMobileMenu,
  toggleMobileMenuOff,
}) => {
  const [isOpenActivitiesMenu, setOpenActivitiesMenu] = useState(false);
  const menuRef = useRef(null);

  const outsideClick = useOutsideClick(menuRef);

  const toggleActivitiesDropdownMenu = () => {
    setOpenActivitiesMenu(!isOpenActivitiesMenu);
  };

  const toggleMenuOff = () => {
    setOpenActivitiesMenu(false);
  };

  useEffect(() => {
    if (!isMobile || !isOpenMobileMenu) {
      setOpenActivitiesMenu(false);
    }
  }, [isMobile, isOpenMobileMenu, outsideClick]);

  return (
    <DropDownMenu
      title={menuData.title}
      isMobile={isMobile}
      toggleActivitiesDropdownMenu={toggleActivitiesDropdownMenu}
      toggleMenuOff={toggleMenuOff}
      isOpenActivitiesMenu={isOpenActivitiesMenu}
      menuRef={menuRef}
    >
      {menuData.links.map((link) => {
        return (
          <MenuItem onClick={toggleMobileMenuOff} key={link.to}>
            <Item to={link.to}>{link.label}</Item>
          </MenuItem>
        );
      })}
    </DropDownMenu>
  );
};

const MenuItem = styled.li.attrs({ className: "border-x-2" })`
  ${tw`first:border-t-2 last:border-b-2`}
`;

const Item = styled(Link)`
  ${tw`flex flex-1 justify-center items-center p-2.5 bg-white italic md:(transition-all ease-in-out hover:bg-gray-200) font-light`}
`;

export default DropMenu;
