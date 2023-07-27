import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { LazyLoadImage } from "react-lazy-load-image-component";

import { PAGES_CREDIT_IMG_BOX } from "../../config/texts";

const ImgBox = ({ img, href, scrollPosition }) => {
  return (
    <Container>
      <ImageSection>
        <Img src={img} scrollPosition={scrollPosition} alt="authorImg" />
      </ImageSection>
      <TextSection>
        <SubTitle>{PAGES_CREDIT_IMG_BOX.title}</SubTitle>
        <Link href={href}>{PAGES_CREDIT_IMG_BOX.link}</Link>
      </TextSection>
    </Container>
  );
};

const Container = styled.div`
  ${tw`flex flex-1 min-w-[100%] sm:min-w-[50%] sm:max-w-[50%] justify-center my-2 2xl:my-4 3xl:my-6`}
`;

const SubTitle = styled.h2`
  ${tw`font-semibold pl-2 3xl:pl-4`}
`;

const Link = styled.a`
  ${tw`text-blue-500 md:hover:opacity-50 transition-all`}
`;

const ImageSection = styled.div`
  ${tw`max-w-[25%] sm:max-w-[35%]`}
`;

const TextSection = styled.div`
  ${tw`flex justify-center items-center flex-col`}
`;

const Img = styled(LazyLoadImage)`
  ${tw`object-contain rounded-lg pointer-events-none`}
`;
export default ImgBox;
