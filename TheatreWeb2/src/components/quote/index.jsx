import React, { useState, useRef } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { motion, useAnimation } from "framer-motion";
import useDraggableScroll from "use-draggable-scroll";

import { HEIGHT, MIN_HEIGHT_BOX } from "./config";

import ArrowDown from "../../assets/arrowDown";

import useAxios from "../../hooks/useAxios";
import useImagePreviews from "../../hooks/useImagePreviews";

import { COMP_QUOTE } from "../../config/texts";
import { GET_ACTIVITIES_FOR_CURRENT_DAY } from "../../config/axios/endpoints";

import ClockSpinner from "../clockSpinner";
import ErrorWarning from "../errorWarning";
import ActivityBox from "./activityBox";

const Quote = () => {
  const [data, error, loading] = useAxios(
    GET_ACTIVITIES_FOR_CURRENT_DAY.method,
    GET_ACTIVITIES_FOR_CURRENT_DAY.url
  );
  const boxRef = useRef(null);
  const images = useImagePreviews(data);
  const boxLoader = useAnimation();
  const rotate = useAnimation();
  const [isClicked, setIsCliked] = useState(false);
  const { onMouseDown } = useDraggableScroll(boxRef);

  const openHandler = () => {
    boxLoader.start({
      height: isClicked ? "0vh" : HEIGHT + "vh",
      minHeight: isClicked ? "0px" : MIN_HEIGHT_BOX + "px",
      transition: { type: "Tween", duration: 0.8 },
    });
    rotate.start({
      borderRadius: isClicked ? "0 0 0.5rem 0.5rem" : "0.5rem 0.5rem 0 0",
      rotate: isClicked ? 0 : 180,
      transition: { type: "Tween", duration: 0.3 },
    });
    setIsCliked(!isClicked);
  };

  return (
    <Wrapper>
      <Container>
        <BoxContainer
          animate={boxLoader}
          ref={boxRef}
          onMouseDown={onMouseDown}
        >
          {loading ? (
            <ClockSpinner
              style={{ "--minHeight": "100%" }}
              loading={loading}
              text={COMP_QUOTE.spinner}
            />
          ) : error ? (
            <ErrorWarning style={{ "--minHeight": "100%" }} />
          ) : data && data.length === 0 ? (
            <ErrorWarning
              title={COMP_QUOTE.errorTitle}
              description={COMP_QUOTE.errorText}
              style={{ "--minHeight": "100%" }}
            />
          ) : (
            data?.map((activity, index) => {
              return (
                <ActivityBox
                  key={index}
                  activity={activity}
                  index={index}
                  isOpen={isClicked}
                  imgUrl={images[index] && images[index].imgUrl}
                />
              );
            })
          )}
        </BoxContainer>

        <TextContainer onClick={openHandler}>
          {COMP_QUOTE.description.first}
          <Bold>{COMP_QUOTE.description.second}</Bold>
          {COMP_QUOTE.description.third}
        </TextContainer>

        <OpenerContainer>
          <Opener onClick={openHandler} animate={rotate}>
            <ArrowBox>
              <ArrowDown style={{ "--width": "60%", "--height": "60%" }} />
            </ArrowBox>
          </Opener>
        </OpenerContainer>
      </Container>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`relative`}
`;

const Container = styled.div`
  ${tw`z-40 absolute w-full`}
`;

const Opener = styled(motion.div).attrs({ className: "bg-slate-300" })`
  ${tw`cursor-pointer z-30 flex justify-center items-center 3xl:p-2 4xl:p-3 rounded-b-lg`}
`;
const TextContainer = styled.section.attrs({ className: "bg-slate-300" })`
  ${tw`z-40 shadow-md text-center overflow-x-auto scrollbar-hide select-none 
   italic font-light cursor-pointer py-2 3xl:py-3 4xl:py-4 whitespace-nowrap text-sm md:text-base lg:text-lg xl:text-base 2xl:text-xl 3xl:text-2xl 4xl:text-3xl transition-all`}
`;

const Bold = styled.span`
  ${tw`font-semibold`}
`;

const OpenerContainer = styled.div`
  ${tw`flex justify-center items-center `}
`;

const ArrowBox = styled.div`
  ${tw`animate-bounce flex flex-1 justify-center items-center`}
`;

const BoxContainer = styled(motion.div).attrs({ className: "bg-slate-400" })`
  ${tw`flex max-w-full w-full overflow-x-auto overflow-y-hidden h-0 max-h-[1000px]`}
`;

export default Quote;
