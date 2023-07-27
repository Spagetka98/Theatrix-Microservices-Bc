import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Colors from "../../config/colors/iconColors";

const MenuIcon = ({
  style,
  fillColor = Colors.white,
  strokeColor = Colors.black,
}) => {
  return (
    <SVG
      xmlns="http://www.w3.org/2000/svg"
      fill={fillColor}
      viewBox="0 0 24 24"
      stroke={strokeColor}
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        strokeWidth={2}
        d="M4 6h16M4 12h16M4 18h16"
      />
    </SVG>
  );
};

const SVG = styled.svg`
  ${tw`h-6 w-6`}
`;

export default MenuIcon;
