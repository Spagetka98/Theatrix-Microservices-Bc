import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import activityDateFormatter from "../../helpersFunc/dateAndTimeFormatter";

const Description = ({ activity, available }) => {
  return (
    <Wrapper>
      {available && (
        <RatingContainer>
          <Rating rating={activity.rating}>{activity.rating}/5</Rating>
        </RatingContainer>
      )}

      <TextContainer>
        <Box>
          <ActivityTitle>{activity.name}</ActivityTitle>

          <AdditionalInformation>
            {activity?.schemalessData?.author && (
              <span>{activity.schemalessData.author}</span>
            )}
            <Container>
              <span>{activity.division}</span>
              <span>{activityDateFormatter(activity)}</span>
            </Container>
          </AdditionalInformation>
        </Box>
      </TextContainer>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`absolute bottom-0 left-0 text-black w-full text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all`}
`;

const RatingContainer = styled.div`
  ${tw`w-full flex justify-end`}
`;

const Rating = styled.span.attrs({ className: "text-orange-400" })(
  ({ rating }) => [
    tw`text-right pt-12 pl-12 pb-6 pr-6 backdrop-blur-xl bg-white/80 font-extrabold rounded-tl-full`,
    rating > 3 && tw`text-green-500`,
    rating < 3 && tw`text-red-500`,
  ]
);

const TextContainer = styled.div`
  ${tw`backdrop-blur-xl bg-white/80 w-full`}
`;

const Box = styled.div`
  ${tw`flex flex-col mx-2`}
`;

const ActivityTitle = styled.span.attrs({
  className: "bg-gradient-to-r from-pink-500 to-violet-500",
})`
  ${tw`bg-clip-text pt-1 text-transparent font-semibold 3xl:py-2`}
`;

const AdditionalInformation = styled.div`
  ${tw`py-1 font-extralight`}
`;

const Container = styled.div`
  ${tw`flex justify-between`}
`;
export default Description;
