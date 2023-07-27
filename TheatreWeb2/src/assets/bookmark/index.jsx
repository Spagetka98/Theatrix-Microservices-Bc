import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Colors from "../../config/colors/iconColors";

const Bookmark = ({
  style,
  fillColor = Colors.white,
  strokeColor = Colors.black,
}) => {
  return (
    <SVG
      style={style}
      xmlns="http://www.w3.org/2000/svg"
      fill={fillColor}
      viewBox="0 0 24 24"
      stroke={strokeColor}
      stroke-width="2"
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z"
      />
    </SVG>
  );
};

const SVG = styled.svg`
  ${tw` min-h-[var(--minHeight,1.5rem)] min-w-[var(--minWidth,1.5rem)] h-[var(--height,70%)] w-[var(--width,70%)] max-h-[var(--maxHeight,5rem)] max-w-[var(--maxHeight,5rem)]`}
`;

export default Bookmark;
