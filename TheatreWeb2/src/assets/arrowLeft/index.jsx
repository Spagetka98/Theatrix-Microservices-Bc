import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Colors from "../../config/colors/iconColors";

const ArrowLeft = ({ style, strokeColor = Colors.black }) => {
  return (
    <SVG
      xmlns="http://www.w3.org/2000/svg"
      style={style}
      viewBox="0 0 24 24"
      strokeWidth={2}
      fill="none"
      stroke={strokeColor}
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M11 19l-7-7 7-7m8 14l-7-7 7-7"
      />
    </SVG>
  );
};
const SVG = styled.svg`
  ${tw`min-h-[var(--minHeight,1.25rem)] min-w-[var(--minWidth,1.25rem)] w-[var(--width,80%)] h-[var(--height,80%)] max-h-[var(--maxHeight,2rem)] max-w-[var(--maxWidth,2rem)]`}
`;
export default ArrowLeft;
