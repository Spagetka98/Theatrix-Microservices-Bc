import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import masks from "../../assets/freepik/masks";
import { COMP_HERO } from "../../config/texts";

const Hero = () => {
  return (
    <Wrapper>
      <LeftContainer>
        <HeroImg src={masks[0].imgUrl} alt="NDM" />
      </LeftContainer>
      <RightContainer>
        <Title>{COMP_HERO.title}</Title>
        <SubTitle>
          <Quote>{COMP_HERO.quote}</Quote>
          <Author>{COMP_HERO.author}</Author>
        </SubTitle>
      </RightContainer>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex min-h-screen`}
`;

const LeftContainer = styled.section`
  ${tw`hidden lg:flex flex-1 relative `}
`;

const HeroImg = styled.img`
  ${tw`hidden lg:block object-contain `}
`;

const RightContainer = styled.section`
  ${tw`flex-1 flex justify-center items-center flex-col `}
`;

const Title = styled.span.attrs({
  className: "bg-gradient-to-r from-pink-500 to-violet-500",
})`
  ${tw`text-center bg-clip-text text-transparent uppercase break-words text-2xl sm:text-3xl md:text-4xl lg:text-5xl xl:text-6xl 2xl:text-7xl 3xl:text-8xl 4xl:text-9xl font-extrabold`}
`;

const SubTitle = styled.div`
  ${tw`flex flex-col mt-2 sm:mt-4 lg:mt-6 xl:mt-10 3xl:mt-12 4xl:mt-14 text-sm sm:text-base md:text-xl lg:text-2xl xl:text-3xl 2xl:text-4xl 3xl:text-5xl 4xl:text-6xl`}
`;

const Quote = styled.span`
  ${tw`flex-1 font-extralight`}
`;

const Author = styled.span`
  ${tw`flex-1 font-extralight italic flex justify-end mt-2 xl:mt-4 3xl:mt-6`}
`;
export default Hero;
