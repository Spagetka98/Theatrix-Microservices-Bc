import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Colors from "../../config/colors/iconColors";

const ExclamationMark = ({
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
        d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
      />
    </SVG>
  );
};

const SVG = styled.svg`
  ${tw` min-h-[var(--minHeight,1.5rem)] min-w-[var(--minWidth,1.5rem)] h-[var(--width,4%)] w-[var(--width,4%)] max-h-[var(--maxHeight,5rem)] max-w-[var(--maxHeight,5rem)]`}
`;

export default ExclamationMark;
