import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Colors from "../../config/colors/iconColors";

const index = ({ style, strokeColor = Colors.black }) => {
  return (
    <SVG
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
      stroke={strokeColor}
      style={style}
      strokeWidth={2}
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z"
      />
    </SVG>
  );
};

const SVG = styled.svg`
  ${tw`min-h-[var(--minHeight,1rem)] min-w-[var(--minWidth,1rem)] w-[var(--width,100%)] h-[var(--height,75%)] max-h-[var(--maxHeight,auto)] max-w-[var(--maxWidth,auto)]`}
`;

export default index;
