import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Colors from "../../config/colors/iconColors";

const ArrowDown = ({
  style,
  fillColor = Colors.black,
  strokeColor = Colors.black,
}) => {
  return (
    <SVG
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 20 20"
      style={style}
      fill={fillColor}
      strokeColor={strokeColor}
    >
      <path
        fillRule="evenodd"
        d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
        clipRule="evenodd"
      />
    </SVG>
  );
};

const SVG = styled.svg`
  ${tw`min-h-[var(--minHeight,1.25rem)] min-w-[var(--minWidth,1.25rem)] w-[var(--width,25%)] h-[var(--height,25%)] max-h-[var(--maxHeight,3rem)] max-w-[var(--maxWidth,3rem)]`}
`;

export default ArrowDown;
