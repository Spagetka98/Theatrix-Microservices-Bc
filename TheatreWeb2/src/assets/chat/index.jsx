import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Colors from "../../config/colors/iconColors";

const Chat = ({
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
      stroke-width="2"
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
      />
    </SVG>
  );
};

const SVG = styled.svg`
  ${tw`min-h-[var(--minHeight,2rem)] min-w-[var(--minWidth,2rem)] w-[var(--width,60%)] h-[var(--height,60%)] max-h-[var(--maxHeight,5rem)] max-w-[var(--maxWidth,5rem)]`}
`;
export default Chat;
