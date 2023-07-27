import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Colors from "../../config/colors/iconColors";

const Facebook = ({
  style,
  fillColor = Colors.darkBlue,
  strokeColor = Colors.black,
}) => {
  return (
    <SVG
      xmlns="http://www.w3.org/2000/svg"
      style={style}
      viewBox="0 0 24 24"
      strokeWidth={0.5}
      fill={fillColor}
      stroke={strokeColor}
    >
      <path d="M9 8h-3v4h3v12h5v-12h3.642l.358-4h-4v-1.667c0-.955.192-1.333 1.115-1.333h2.885v-5h-3.808c-3.596 0-5.192 1.583-5.192 4.615v3.385z" />
    </SVG>
  );
};
const SVG = styled.svg`
  ${tw`min-h-[var(--minHeight,1.25rem)] min-w-[var(--minWidth,1.25rem)] w-[var(--width,50%)] h-[var(--height,50%)] max-h-[var(--maxHeight,5rem)] max-w-[var(--maxWidth,5rem)]`}
`;
export default Facebook;
