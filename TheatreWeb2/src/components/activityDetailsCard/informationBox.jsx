import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { Link } from "react-router-dom";

import activityDateFormatter from "../../helpersFunc/dateAndTimeFormatter";

import { COMP_ACTIVITIES_DETAILS_CARD } from "../../config/texts";
import { ACITIVITY_RATINGS } from "../../config/navigation/paths";

import RatingBox from "../../components/ratingBox";

import Toolbar from "./toolbar";
import TextHint from "./textHint";

const InformationBox = ({ id, data, available, ratingBoxRef, isMobile }) => {
  return (
    <>
      <Title>{data?.name}</Title>
      <Division>{data?.division}</Division>
      <InformationContainer>
        <div>
          {data?.schemalessData?.author && (
            <TextHint
              title={COMP_ACTIVITIES_DETAILS_CARD.authors}
              text={data.schemalessData.author}
            />
          )}
          <TextHint
            title={COMP_ACTIVITIES_DETAILS_CARD.stage}
            text={data?.stage}
          />
          <TextHint
            title={COMP_ACTIVITIES_DETAILS_CARD.date}
            text={activityDateFormatter(data)}
          />
          {data?.schemalessData?.url && (
            <TextHint
              title={COMP_ACTIVITIES_DETAILS_CARD.otherInfo}
              text={
                <LinkBlue
                  href={"https://" + data.schemalessData.url}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  {COMP_ACTIVITIES_DETAILS_CARD.link}
                </LinkBlue>
              }
            />
          )}

          {isMobile && (
            <Toolbar
              id={id}
              data={data}
              available={available}
              ratingBoxRef={ratingBoxRef}
            />
          )}
        </div>

        {available && (
          <LinkWrapper>
            <StyledLink to={ACITIVITY_RATINGS + "/" + id}>
              {COMP_ACTIVITIES_DETAILS_CARD.ratingsLinks}
            </StyledLink>
          </LinkWrapper>
        )}
        <RatingBox
          id={id}
          data={data}
          available={available}
          ref={ratingBoxRef}
        />
      </InformationContainer>
    </>
  );
};
const Title = styled.div.attrs({
  className: "bg-gradient-to-r from-pink-500 to-violet-500",
})`
  ${tw`border-b-4 border-pink-200 pb-5 pt-6 text-center bg-clip-text text-transparent font-extrabold transition-all text-xl sm:text-2xl md:text-3xl lg:text-4xl 2xl:text-5xl 3xl:text-7xl 4xl:text-9xl`}
`;

const Division = styled.div`
  ${tw`w-full text-center py-2 italic text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all font-extralight`}
`;

const InformationContainer = styled.div`
  ${tw`flex flex-col flex-1 justify-between mr-4 xl:mr-8 3xl:mr-12 text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all font-extralight`}
`;

const LinkBlue = styled.a.attrs({ className: "w-fit" })`
  ${tw`text-blue-500 ml-1 lg:hover:opacity-80 flex items-center`}
`;

const LinkWrapper = styled.div`
  ${tw`flex justify-center items-center my-4 lg:my-2`}
`;

const StyledLink = styled(Link)`
  ${tw`text-white hover:opacity-90 font-extrabold  transition-all text-center py-2 px-4 3xl:py-8 3xl:px-16 rounded-lg bg-blue-500 `}
`;

export default InformationBox;
