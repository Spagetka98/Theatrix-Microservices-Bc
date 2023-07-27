import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Colors from "../../config/colors/iconColors";

const ArrowRight = ({ style, strokeColor = Colors.black, strokeWidth = 2 }) => {
  return (
    <SVG
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      style={style}
      viewBox="0 0 24 24"
      stroke={strokeColor}
      strokeWidth={strokeWidth}
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M13 5l7 7-7 7M5 5l7 7-7 7"
      />
    </SVG>
  );
};

const SVG = styled.svg`
  ${tw`min-h-[var(--minHeight,1.25rem)] min-w-[var(--minWidth,1.25rem)] w-[var(--width,80%)] h-[var(--height,80%)] max-h-[var(--maxHeight,2rem)] max-w-[var(--maxWidth,2rem)]`}
`;
export default ArrowRight;
