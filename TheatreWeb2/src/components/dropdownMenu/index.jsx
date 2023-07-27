import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import ArrowDown from "../../assets/arrowDown";

const DropDownMenu = ({
  title,
  isMobile,
  toggleActivitiesDropdownMenu,
  toggleMenuOff,
  isOpenActivitiesMenu,
  children,
  menuRef,
}) => {
  return (
    <Wrapper ref={menuRef}>
      <DropButton onClick={toggleActivitiesDropdownMenu}>
        <Title>{title}</Title>
        <ArrowDown style={{ "--width": "3%", "--height": "3%" }} />
      </DropButton>
      <DropMenu
        onClick={isMobile ? null : toggleMenuOff}
        isOpenActivitiesMenu={isOpenActivitiesMenu}
      >
        {children}
      </DropMenu>
    </Wrapper>
  );
};

const Wrapper = styled.div.attrs({ className: "group" })`
  ${tw`md:(relative mx-2) 3xl:py-8 `}
`;

const DropButton = styled.button`
  ${tw`flex w-full justify-center items-center h-full p-3 md:(p-0 px-4)`}
`;

const Title = styled.span`
  ${tw`mr-1 text-sm sm:text-lg 2xl:text-xl 3xl:text-2xl 4xl:text-3xl md:group-hover:opacity-60 transition-all font-light`}
`;

const DropMenu = styled.ul(({ isOpenActivitiesMenu }) => [
  tw`min-w-full z-50 3xl:mt-8 transition-all text-xs sm:text-base hidden md:(absolute group-hover:block) 2xl:text-lg 3xl:text-xl 4xl:text-2xl`,
  isOpenActivitiesMenu ? tw`block` : tw`hidden`,
]);

export default DropDownMenu;
