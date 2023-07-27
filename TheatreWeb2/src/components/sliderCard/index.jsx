import React from "react";
import { motion } from "framer-motion";
import styled from "styled-components";
import tw from "twin.macro";

import TextHint from "./textHint";

import { COMP_SLIDER_CARD } from "../../config/texts";

import activityDateFormatter from "../../helpersFunc/dateAndTimeFormatter";
import CounterSection from "./counterSection";
import LinkSection from "./linkSection";

const SliderCard = ({ activity, imgUrl }) => {
  return (
    <Wrapper>
      <ImgSection>
        <Img
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ type: "spring", duration: 1.5 }}
          src={imgUrl}
          alt="previewImg"
        />
        <Separator />
      </ImgSection>

      <TextSection>
        <Title
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ type: "spring", duration: 0.8, delay: 0.25 }}
        >
          {activity.name}
        </Title>
        <Category
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ type: "spring", duration: 0.8, delay: 0.7 }}
        >
          {activity.division}
        </Category>

        <HintSection
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ type: "spring", duration: 0.8, delay: 1.15 }}
        >
          {activity?.schemalessData?.author && (
            <TextHint
              title={COMP_SLIDER_CARD.authors}
              text={activity.schemalessData.author}
            />
          )}
          <TextHint title={COMP_SLIDER_CARD.stage} text={activity.stage} />
          <TextHint
            title={COMP_SLIDER_CARD.dateOfActivity}
            text={activityDateFormatter(activity)}
          />
          <TextHint
            title={COMP_SLIDER_CARD.rating}
            text={activity.rating + " / " + COMP_SLIDER_CARD.totalRating}
          />
        </HintSection>

        <LinkSection
          url={activity?.schemalessData?.url}
          id={activity.idActivity}
        />

        <CounterSection
          totalLiked={activity.totalLiked}
          totalRatings={activity.totalRatings}
          totalDisliked={activity.totalDisliked}
        />
      </TextSection>
    </Wrapper>
  );
};

const Wrapper = styled.section`
  ${tw`w-full flex bg-white select-none flex-col sm:flex-row`}
`;

const ImgSection = styled.div.attrs({ className: "flex-[3_3_0%]" })`
  ${tw` relative pointer-events-none min-h-[300px]`}
`;

const Img = styled(motion.img)`
  ${tw`w-full h-full object-cover absolute`}
`;

const Separator = styled.div.attrs({ className: "shadow-l-lg" })`
  ${tw`absolute z-10 w-full h-2 bottom-0 rounded-t-full sm:rounded-t-none sm:w-2 sm:h-full sm:top-0 sm:right-0 sm:rounded-l-full bg-white `}
`;

const TextSection = styled.div.attrs({ className: "flex-[7_7_0%]" })`
  ${tw`flex flex-col `}
`;

const Title = styled(motion.div)`
  ${tw`text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all text-center py-3 md:mr-2 font-semibold border-b-2 3xl:border-b-4 border-pink-200 `}
`;

const Category = styled(motion.div)`
  ${tw`text-sm md:text-base lg:text-lg 3xl:text-xl 4xl:text-2xl transition-all text-center font-extralight`}
`;

const HintSection = styled(motion.div)`
  ${tw`ml-2 my-4 sm:ml-0 sm:my-0 sm:py-2 md:py-3 lg:py-5 xl:py-10`}
`;
export default SliderCard;
