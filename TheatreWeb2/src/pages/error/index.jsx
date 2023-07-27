import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import error from "../../assets/freepik/error";

import { PAGES_ERROR } from "../../config/texts";

const ErrorPage = () => {
  return (
    <Wrapper>
      <ImgContainer>
        <Img src={error[0].imgUrl} alt="errorImg" />
      </ImgContainer>
      <TextContainer>
        <Title>{PAGES_ERROR.title}</Title>
        <Text>{PAGES_ERROR.text}</Text>
      </TextContainer>
    </Wrapper>
  );
};

const Wrapper = styled.section`
  ${tw`min-h-screen flex flex-col lg:flex-row`}
`;

const ImgContainer = styled.div`
  ${tw`flex-1 flex justify-center items-center mt-6`}
`;

const Img = styled.img`
  ${tw`object-contain`}
`;

const TextContainer = styled.div`
  ${tw`flex-1 flex justify-center px-4 flex-col items-center`}
`;

const Title = styled.span`
  ${tw`font-bold md:py-4  transition-all text-lg sm:text-xl md:text-2xl lg:text-3xl 2xl:text-4xl 3xl:text-6xl 4xl:text-7xl`}
`;

const Text = styled.span`
  ${tw`py-2 text-center lg:text-left text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all font-extralight`}
`;

export default ErrorPage;
