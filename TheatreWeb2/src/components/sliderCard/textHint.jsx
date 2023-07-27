import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Bookmark from "../../assets/bookmark";

import Colors from "../../config/colors/iconColors";

const TextHint = ({ title, text }) => {
  return (
    <Wrapper>
      <IconWrapper>
        <Bookmark
          fillColor={Colors.lightOrange}
          strokeColor={Colors.darkOrange}
        />
      </IconWrapper>

      <TextWrapper>
        <Bold>{title}</Bold>
        <Text>{text}</Text>
      </TextWrapper>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex  text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all font-extralight `}
`;

const IconWrapper = styled.div`
  ${tw`flex flex-1 justify-center items-center md:py-1 3xl:py-2 4xl:py-3`}
`;

const TextWrapper = styled.div.attrs({ className: "flex-[9_9_0%]" })`
  ${tw`flex items-center ml-1 2xl:ml-2 3xl:ml-3 4xl:ml-4`}
`;

const Bold = styled.span`
  ${tw`font-semibold pr-2 3xl:pr-3`}
`;

const Text = styled.span`
  ${tw`break-all pr-2`}
`;
export default TextHint;
