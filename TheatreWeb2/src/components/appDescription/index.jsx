import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { useMediaQuery } from "react-responsive";

import masks from "../../assets/freepik/masks";

import Description from "./description";
import LinkBox from "./linkBox";

import { TABLET } from "../../config/sizes";
import { COMP_APP_DESCRIPTION } from "../../config/texts";

const AppDescription = () => {
  const isMobile = useMediaQuery({
    query: TABLET,
  });
  return (
    <Wrapper>
      <Container>
        <LeftContainer>
          <Article>
            <Title>{COMP_APP_DESCRIPTION.title}</Title>

            <Description isMobile={isMobile} />

            <Divider />

            <LinkBox />
          </Article>
        </LeftContainer>
        {!isMobile && (
          <RightContainer>
            <Image src={masks[1].imgUrl} alt="MaskDesc" isMobile={isMobile} />
          </RightContainer>
        )}
      </Container>
    </Wrapper>
  );
};
const Wrapper = styled.div`
  ${tw`flex items-center justify-center`}
`;

const Container = styled.section`
  ${tw`flex min-h-[50vh] justify-center items-center cursor-default`}
`;

const LeftContainer = styled.div`
  ${tw`flex-1 m-4 md:m-6 lg:m-8 2xl:m-10 4xl:m-12`}
`;

const RightContainer = styled.div`
  ${tw`flex-1 flex justify-center items-center `}
`;

const Article = styled.article`
  ${tw`h-full p-3`}
`;

const Image = styled.img(({ isMobile }) => [
  tw`object-contain`,
  isMobile && tw`max-h-[400px]`,
]);

const Title = styled.h1.attrs({
  className: "decoration-2 decoration-pink-500",
})`
  ${tw`font-bold underline transition-all text-center text-xl sm:text-2xl md:text-3xl lg:text-4xl lg:text-left 2xl:text-5xl 3xl:text-7xl 4xl:text-9xl`}
`;

const Divider = styled.div`
  ${tw`border-b-2 lg:border-b-4 border-gray-300 py-2 3xl:py-4`}
`;

export default AppDescription;
