import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { useMediaQuery } from "react-responsive";
import { motion } from "framer-motion";

import Button from "./button";
import DayPicker from "./dayPicker";
import DateToggle from "./dateToggle";

import Colors from "../../config/colors/ratingButton";
import { COMP_RATING_BAR } from "../../config/texts";
import { TABLET } from "../../config/sizes";

import Actions from "./actions";

const RatingBar = ({ state, dispatch, errorMessage }) => {
  const isTablet = useMediaQuery({
    query: TABLET,
  });

  return (
    <Wrapper>
      <Container>
        <RatingsWrapper>
          {[...Array(5)].map((number, index) => {
            return (
              <Button
                key={index}
                color={Colors[index]}
                rating={index + 1}
                dispatch={dispatch}
                active={state?.ratings?.includes(index + 1)}
              />
            );
          })}
        </RatingsWrapper>
        <DaysWrapper>
          <DayPicker
            title={COMP_RATING_BAR.from}
            state={state?.startDate}
            dispatch={dispatch}
            active={state?.dateFiltration}
            action={Actions.SET_START_DATE}
          />
          {!isTablet && (
            <ToggleWrapper>
              <DateToggle
                dispatch={dispatch}
                dateFiltration={state?.dateFiltration}
              />
            </ToggleWrapper>
          )}
          <DayPicker
            title={COMP_RATING_BAR.to}
            plusDays={7}
            state={state?.endDate}
            dispatch={dispatch}
            active={state?.dateFiltration}
            action={Actions.SET_END_DATE}
          />
          {isTablet && (
            <ToggleWrapper>
              <DateToggle
                dispatch={dispatch}
                dateFiltration={state?.dateFiltration}
              />
            </ToggleWrapper>
          )}
        </DaysWrapper>

        {errorMessage && (
          <Error
            initial={{ opacity: 0 }}
            animate={{
              opacity: errorMessage ? 1 : 0,
            }}
            transition={{
              type: "spring",
              duration: errorMessage ? 2 : 0,
            }}
          >
            {errorMessage}
          </Error>
        )}
      </Container>
    </Wrapper>
  );
};

const Wrapper = styled.div.attrs({
  className: "bg-slate-100 border-slate-300",
})`
  ${tw`items-center w-full border-b-2 pt-20 3xl:pt-32 flex flex-col shadow-md font-light text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all`}
`;

const Container = styled.div`
  ${tw`flex flex-col w-full pb-3`}
`;

const DaysWrapper = styled.div`
  ${tw` flex flex-col lg:flex-row lg:justify-around`}
`;

const RatingsWrapper = styled.div`
  ${tw`flex overflow-x-auto`}
`;

const ToggleWrapper = styled.div`
  ${tw`flex justify-center items-center`}
`;

const Error = styled(motion.span)`
  ${tw`text-red-400 w-full text-center text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-2xl 4xl:text-3xl transition-all`}
`;
export default RatingBar;
