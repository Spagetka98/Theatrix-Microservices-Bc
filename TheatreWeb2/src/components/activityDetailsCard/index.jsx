import React, { useRef, useCallback } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { useMediaQuery } from "react-responsive";

import useTimer from "../../hooks/useTimer";

import { TABLET } from "../../config/sizes";

import Toolbar from "./toolbar";
import InformationBox from "./informationBox";
import SideRatingBox from "../sideRatingBox";

const ActivityDetailsCard = ({ id, data }) => {
  const ratingBoxRef = useRef(null);
  const available = useTimer(data.available, data.milliSecLeft);
  const isMobile = useMediaQuery({
    query: TABLET,
  });
  const realoadRatingBox = useCallback(() => {
    ratingBoxRef && ratingBoxRef.current.setAxios();
  }, []);

  return (
    <Wrapper>
      {available && <SideRatingBox id={id} realoadHandler={realoadRatingBox} />}
      <Container>
        {!isMobile && (
          <LeftContainer>
            <Toolbar id={id} data={data} available={available} />
          </LeftContainer>
        )}

        <RightContainer>
          <InformationBox
            id={id}
            data={data}
            available={available}
            ratingBoxRef={ratingBoxRef}
            isMobile={isMobile}
          />
        </RightContainer>
      </Container>

      {data?.schemalessData?.description && (
        <Description>
          <Title>Něco málo o akci...</Title>
          {data.schemalessData.description}
        </Description>
      )}
    </Wrapper>
  );
};
const Wrapper = styled.div`
  ${tw`relative`}
`;

const Container = styled.div`
  ${tw`flex flex-1 mt-20`}
`;

const LeftContainer = styled.div`
  ${tw`flex-1 flex flex-col`}
`;

const RightContainer = styled.div`
  ${tw`flex-1 flex flex-col mx-8`}
`;

const Title = styled.div`
  ${tw`font-bold mt-8 mb-2 border-b-4`}
`;

const Description = styled.div`
  ${tw`ml-6 mb-8 mr-10 xl:mr-12 3xl:mr-16 whitespace-pre-line leading-relaxed text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all font-extralight`}
`;

export default ActivityDetailsCard;
