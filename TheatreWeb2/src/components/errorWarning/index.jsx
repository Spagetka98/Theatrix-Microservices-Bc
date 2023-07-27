import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Smiley from "../../assets/smiley";

import { COMP_ERROR_WARNING } from "../../config/texts";

const ErrorWarning = ({
  style,
  title = COMP_ERROR_WARNING.title,
  description = COMP_ERROR_WARNING.description,
  hideImg,
  hideTitle,
}) => {
  return (
    <Wrapper style={style}>
      {!hideImg && (
        <ImgContainer>
          <Smiley />
        </ImgContainer>
      )}
      {!hideTitle && <Title>{title}</Title>}
      <SubTitle>{description}</SubTitle>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex-1 flex justify-center items-center flex-col min-h-[var(--minHeight,50vh)]`}
`;

const ImgContainer = styled.div`
  ${tw`flex justify-center items-center animate-bounce m-4`}
`;

const Title = styled.h1`
  ${tw`font-bold text-center transition-all text-xl sm:text-2xl md:text-3xl lg:text-4xl 2xl:text-5xl 3xl:text-6xl 4xl:text-7xl`}
`;

const SubTitle = styled.h2`
  ${tw`font-light text-center mt-3 italic transition-all text-base sm:text-lg md:text-xl lg:text-2xl 3xl:text-3xl 4xl:text-4xl`}
`;
export default ErrorWarning;
