import React, { useState, useRef, useEffect } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { useMediaQuery } from "react-responsive";
import { motion } from "framer-motion";

import { MOBILE, TABLET } from "../../config/sizes";
import { COMP_SIDE_RATING_BOX } from "../../config/texts";

import useOutsideClick from "../../hooks/useOutsideClick";

import RatingForm from "./ratingForm";

const SideRatingBox = ({ id, realoadHandler }) => {
  const box = useRef(null);
  const outSideClick = useOutsideClick(box);
  const [isOpen, setIsOpen] = useState(false);
  const isMobile = useMediaQuery({
    query: MOBILE,
  });
  const isTablet = useMediaQuery({
    query: TABLET,
  });
  const openHandler = () => {
    setIsOpen(!isOpen);
  };
  useEffect(() => {
    if (outSideClick) {
      setIsOpen(false);
    }
  }, [outSideClick]);

  return (
    <Wrapper
      ref={box}
      ismobile={isMobile ? 1 : 0}
      initial={{ width: "auto" }}
      animate={{ width: isOpen ? (isTablet ? "100%" : "60%") : "auto" }}
      transition={{ type: "spring", duration: 1 }}
    >
      <Container>
        {!isOpen && (
          <Opener onClick={openHandler}>
            {COMP_SIDE_RATING_BOX.title.split("").map((letter, index) => {
              return <Letter key={index}>{letter}</Letter>;
            })}
          </Opener>
        )}

        {isOpen && (
          <Body>
            <RatingForm
              id={id}
              realoadHandler={realoadHandler}
              openHandler={openHandler}
            />
          </Body>
        )}
      </Container>
    </Wrapper>
  );
};

const Wrapper = styled(motion.div).attrs({ className: "group" })`
  ${tw`fixed bottom-[2%] h-[65%] right-0 shadow-xl backdrop-blur-xl bg-white/95 lg:rounded-l-xl overflow-hidden z-20 min-h-[200px] lg:min-h-[300px]`}
`;

const Container = styled.div`
  ${tw`flex h-full w-full`}
`;

const Opener = styled.div.attrs({
  className:
    "bg-gradient-to-r from-pink-500 to-violet-500 bg-clip-text text-transparent",
})`
  ${tw`flex flex-col uppercase border-4 4xl:border-8 font-extrabold rounded-l-xl cursor-pointer group-hover:border-purple-500 justify-center items-center overflow-hidden text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all`}
`;

const Letter = styled.span`
  ${tw`flex-1 flex justify-center items-center px-2 xl:px-3 3xl:px-5 4xl:px-6`}
`;

const Body = styled.div.attrs({
  className: "border-y-4 lg:border-l-4 4xl:border-y-8 4xl:border-l-8",
})`
  ${tw`w-full lg:rounded-l-xl group-hover:border-purple-500 `}
`;
export default SideRatingBox;
