import React, { useState } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { motion } from "framer-motion";
import { useMediaQuery } from "react-responsive";

import SliderButton from "./sliderButton";
import SliderCard from "../sliderCard";

import ArrowRight from "../../assets/arrowRight";
import ArrowLeft from "../../assets/arrowLeft";
import useImagePreviews from "../../hooks/useImagePreviews";

import { TABLET } from "../../config/sizes";

const Slider = ({ activities }) => {
  const [currentSlide, setCurrentSlide] = useState(0);
  const [touchStart, setTouchStart] = useState(null);
  const [touchEnd, setTouchEnd] = useState(null);
  const images = useImagePreviews(activities);

  const isMobile = useMediaQuery({
    query: TABLET,
  });

  const nextSlide = () => {
    setCurrentSlide((c) => (c === activities.length - 1 ? 0 : c + 1));
  };

  const prevSlide = () => {
    setCurrentSlide((c) => (c === 0 ? activities.length - 1 : c - 1));
  };

  const handleMouseClickStart = (e) => {
    setTouchStart(e.clientX);
  };

  const handleMouseClickEnd = (e) => {
    setTouchEnd(e.clientX);
  };

  const handleTouchEnd = (e) => {
    setTouchStart(e.targetTouches[0].clientX);
  };

  const handleTouchStart = (e) => {
    setTouchEnd(e.targetTouches[0].clientX);
  };

  const handleSwipe = () => {
    if (touchStart == null || touchEnd == null) return;

    if (touchStart - touchEnd > 75) {
      nextSlide();
    }

    if (touchStart - touchEnd < -75) {
      prevSlide();
    }

    setTouchStart(null);
    setTouchEnd(null);
  };

  if (!Array.isArray(activities) || activities.length <= 0) {
    return null;
  }

  return (
    <Wrapper>
      {!isMobile && (
        <BtnWrapper>
          <SliderButton onClick={prevSlide}>
            <ArrowLeft />
          </SliderButton>
        </BtnWrapper>
      )}

      {activities.map((activity, index) => {
        return (
          index === currentSlide && (
            <Container
              initial={{ scale: 0.95 }}
              animate={{ scale: 1 }}
              transition={{ type: "spring", duration: 0.5 }}
              whileTap={{ scale: 0.98 }}
              onTouchStart={handleTouchStart}
              onTouchMove={handleTouchEnd}
              onTouchEnd={handleSwipe}
              onMouseDown={handleMouseClickEnd}
              onMouseMove={handleSwipe}
              onMouseUp={handleMouseClickStart}
              key={index}
            >
              <SliderCard
                activity={activity}
                imgUrl={images[index] && images[index].imgUrl}
              />

              <ItemCounter>
                {activities.map((activity, index) => {
                  return <Circle key={index} active={index === currentSlide} />;
                })}
              </ItemCounter>
            </Container>
          )
        );
      })}

      {!isMobile && (
        <BtnWrapper>
          <SliderButton onClick={nextSlide}>
            <ArrowRight />
          </SliderButton>
        </BtnWrapper>
      )}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex flex-1 justify-center py-4 lg:py-8 2xl:py-16 3xl:py-20`}
`;

const BtnWrapper = styled.div`
  ${tw`flex-1 flex justify-center items-center`}
`;

const ItemCounter = styled.div`
  ${tw`absolute bottom-0 w-full justify-center items-center flex`}
`;

const Container = styled(motion.div).attrs({
  className: "flex-[9_9_0%] shadow-[0px_5px_15px_rgba(0,0,0,0.35)]",
})`
  ${tw`flex relative overflow-hidden m-4 rounded-2xl`}
`;

const Circle = styled.div`
  ${tw`rounded-full p-1 2xl:p-2 3xl:p-3 4xl:p-4 transition-all m-2 shadow-md`};
  ${({ active }) => (active ? tw`bg-gray-300` : tw`bg-gray-500`)}
`;
export default Slider;
