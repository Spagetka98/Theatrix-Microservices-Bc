import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Colors from "../../config/colors/iconColors";

const ThumbDown = ({
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
      strokeWidth={2}
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M10 14H5.236a2 2 0 01-1.789-2.894l3.5-7A2 2 0 018.736 3h4.018a2 2 0 01.485.06l3.76.94m-7 10v5a2 2 0 002 2h.096c.5 0 .905-.405.905-.904 0-.715.211-1.413.608-2.008L17 13V4m-7 10h2m5-10h2a2 2 0 012 2v6a2 2 0 01-2 2h-2.5"
      />
    </SVG>
  );
};

const SVG = styled.svg`
  ${tw`min-h-[var(--minHeight,2rem)] min-w-[var(--minWidth,2rem)] w-[var(--width,60%)] h-[var(--height,60%)] max-h-[var(--maxHeight,5rem)] max-w-[var(--maxWidth,5rem)]`}
`;

export default ThumbDown;
