import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import masks from "../../assets/freepik/masks";

import TextHint from "./textHint";

import { COMP_APP_DESCRIPTION } from "../../config/texts";

const Description = ({ isMobile }) => {
  return (
    <>
      <UpperDescription>
        {COMP_APP_DESCRIPTION.upperText.first}
        <Bold>{COMP_APP_DESCRIPTION.upperText.second}</Bold>
        {COMP_APP_DESCRIPTION.upperText.third}
      </UpperDescription>
      {isMobile && (
        <RightContainer>
          <Image src={masks[1].imgUrl} alt="MaskDesc" isMobile={isMobile} />
        </RightContainer>
      )}
      <SubTitle> {COMP_APP_DESCRIPTION.subTitle}</SubTitle>

      <TextHint text={COMP_APP_DESCRIPTION.possibilities.first} />

      <TextHint text={COMP_APP_DESCRIPTION.possibilities.second} />

      <TextHint text={COMP_APP_DESCRIPTION.possibilities.third} />

      <LowerDescription>{COMP_APP_DESCRIPTION.lowerText}</LowerDescription>
    </>
  );
};

const RightContainer = styled.div`
  ${tw`flex-1 flex justify-center items-center `}
`;

const Image = styled.img(({ isMobile }) => [
  tw`object-contain`,
  isMobile && tw`max-h-[400px]`,
]);

const UpperDescription = styled.p`
  ${tw`py-4 3xl:py-8 text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all`}
`;

const LowerDescription = styled.p`
  ${tw`pt-2 3xl:pt-4 text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all`}
`;

const SubTitle = styled.h2`
  ${tw`pb-4 3xl:pb-6 font-bold text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all`}
`;

const Bold = styled.span`
  ${tw`font-bold`}
`;

export default Description;
