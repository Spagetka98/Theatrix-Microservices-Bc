import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

const smiley = () => {
  return (
    <SVG
      xmlns="http://www.w3.org/2000/svg"
      fill="#ffcc43"
      viewBox="0 0 24 24"
      stroke="currentColor"
      strokeWidth={2}
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
      />
    </SVG>
  );
};

const SVG = styled.svg`
  ${tw`min-h-[3rem] min-w-[3rem] w-[25%] h-[25%] max-h-[25rem] max-w-[25rem]`}
`;

export default smiley;
