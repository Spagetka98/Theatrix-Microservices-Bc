import React, { useEffect } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { motion, useAnimation } from "framer-motion";

import useTimer from "../../hooks/useTimer";
import activityDateFormatter from "../../helpersFunc/dateAndTimeFormatter";

import { COMP_QUOTE_ACTIVITY_BOX } from "../../config/texts";

const ActivityBox = ({ activity, index, isOpen = false, imgUrl }) => {
  const activityLoader = useAnimation();
  const available = useTimer(activity.available, activity.milliSecLeft);

  useEffect(() => {
    const durationT = isOpen ? 2 + index : 0;
    const delayT = isOpen ? 1 + index : 1;
    activityLoader.start({
      opacity: isOpen ? (available ? 0.5 : 1) : 0,
      transition: { type: "spring", duration: durationT, delay: delayT },
    });
  }, [isOpen, activityLoader, index, available]);

  return (
    <Wrapper
      animate={activityLoader}
      whileHover={{ scale: 0.98 }}
      transition={{ type: "spring", duration: 1 }}
    >
      <ImgContainer>
        <Img
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ type: "spring", duration: 1.5 }}
          src={imgUrl}
          alt="previewImg"
        />
      </ImgContainer>

      <TextContainer>
        <Title>{activity.name}</Title>
        <Category>{activity.division}</Category>

        <DescribeContainer>
          {activity.schemalessData?.author && (
            <Box>
              <TextTitle>{COMP_QUOTE_ACTIVITY_BOX.authors}</TextTitle>
              {activity.schemalessData.author}
            </Box>
          )}
          <Box>
            <TextTitle>{COMP_QUOTE_ACTIVITY_BOX.date}</TextTitle>
            {activityDateFormatter(activity)}
          </Box>
          {!available && activity?.schemalessData?.url && (
            <Box>
              <TextTitle>{COMP_QUOTE_ACTIVITY_BOX.tickets}</TextTitle>
              <Link
                href={"https://" + activity.schemalessData.url}
                target="_blank"
                rel="noopener noreferrer"
              >
                {COMP_QUOTE_ACTIVITY_BOX.linkName}
              </Link>
            </Box>
          )}
          <InfromationText available={available}>
            {available
              ? COMP_QUOTE_ACTIVITY_BOX.available
              : COMP_QUOTE_ACTIVITY_BOX.unavailable}
          </InfromationText>
        </DescribeContainer>
      </TextContainer>
    </Wrapper>
  );
};

const Wrapper = styled(motion.section).attrs({ className: "bg-slate-100" })`
  ${tw`min-w-[500px] lg:min-w-[60vw] select-none font-extralight opacity-0 shadow-md mx-4 3xl:mx-8 my-8 flex overflow-hidden rounded-lg`}
`;

const ImgContainer = styled.div`
  ${tw`flex-1`}
`;

const Img = styled(motion.img)`
  ${tw`w-full h-full object-cover border-r-4 pointer-events-none`}
`;

const TextContainer = styled.div.attrs({
  className: "flex-[2_2_0%]",
})`
  ${tw`flex flex-col text-base lg:text-lg 2xl:text-2xl 3xl:text-3xl 4xl:text-4xl transition-all`}
`;

const Title = styled.span`
  ${tw`border-b-2 border-b-pink-300 mx-2 p-1 3xl:p-3 text-center font-semibold`}
`;

const Category = styled.span`
  ${tw`text-center italic`}
`;

const DescribeContainer = styled.div`
  ${tw`flex flex-col flex-1 overflow-x-hidden overflow-y-auto`}
`;

const Box = styled.div`
  ${tw`flex-1 flex items-center`}
`;

const TextTitle = styled.span`
  ${tw`font-semibold px-2`}
`;

const Link = styled(motion.a)`
  ${tw`text-blue-500 md:hover:opacity-50 transition-all`}
`;

const InfromationText = styled.div(({ available }) => [
  tw`flex-1 font-semibold text-green-600 italic flex justify-center items-center`,
  available ? tw`text-red-600` : tw`text-green-600`,
]);
export default ActivityBox;
