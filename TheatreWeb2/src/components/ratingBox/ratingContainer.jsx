import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import RatingBar from "./ratingBar";

import Colors from "../../config/colors/ratingBox";
import { COMP_RATING_BOX_RATING_CONTAINER } from "../../config/texts";

const RatingContainer = ({ totalRatings, totalRating, ratings }) => {
  return (
    <Wrapper>
      <Title>{COMP_RATING_BOX_RATING_CONTAINER.title}</Title>
      <Text>
        {totalRatings === 0 ? (
          COMP_RATING_BOX_RATING_CONTAINER.noRatings
        ) : (
          <>
            {COMP_RATING_BOX_RATING_CONTAINER.ratingsExistParts.first}
            <Bold>{totalRatings}</Bold>
            {(totalRatings === 1
              ? COMP_RATING_BOX_RATING_CONTAINER.ratingsExistParts.rating
              : COMP_RATING_BOX_RATING_CONTAINER.ratingsExistParts.ratings) +
              COMP_RATING_BOX_RATING_CONTAINER.ratingsExistParts.is}
            <Bold>{totalRating}</Bold>
          </>
        )}
      </Text>
      {ratings?.length > 0 &&
        [...Array(ratings?.length)].map((value, index) => {
          const filling = (ratings[index] / totalRatings) * 100;
          return (
            <RatingBar
              key={index}
              numberOfStar={ratings?.length - index}
              filling={filling || 0}
              totalCount={ratings[index]}
              fillingColor={Colors[index]}
            />
          );
        })}
    </Wrapper>
  );
};
const Wrapper = styled.div`
  ${tw`flex flex-col hover:(border-purple-500 border-2 xl:border-4 shadow-2xl)  text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all font-extralight border-2 xl:border-4 shadow-lg rounded-3xl pb-4`}
`;

const Title = styled.div.attrs({
  className: "bg-gradient-to-r from-pink-500 to-violet-500",
})`
  ${tw` pb-5 pt-6 text-center bg-clip-text text-transparent font-extrabold transition-all text-lg sm:text-xl md:text-xl lg:text-2xl 2xl:text-3xl 3xl:text-4xl 4xl:text-5xl`}
`;

const Text = styled.p`
  ${tw`text-center mb-2`}
`;

const Bold = styled.span`
  ${tw`font-bold`}
`;
export default RatingContainer;
