import React from "react";
import styled from "styled-components";
import { motion } from "framer-motion";
import tw from "twin.macro";
import { useDispatch } from "react-redux";
import { useMediaQuery } from "react-responsive";

import DateToggle from "./dateToggle";
import DayPicker from "./dayPicker";
import DivisionBar from "../divisionBar";

import { COMP_FILTER_BAR } from "../../config/texts";
import { TABLET } from "../../config/sizes";

const FilterBar = ({
  error,
  state,
  addActivityNameHandler,
  addAuthorNameHandler,
  addStartDateHandler,
  addEndDateHandler,
  addDivisionHandler,
  removeDivisionHandler,
  addPageHandler,
  setDateFiltration,
}) => {
  const isTablet = useMediaQuery({
    query: TABLET,
  });

  const dispatch = useDispatch();

  const activityNameHandler = (e) => {
    let value = e.target.value.trim();

    if (value.length >= 3) {
      dispatch(addActivityNameHandler(value));
      dispatch(addPageHandler(1));
    } else {
      dispatch(addActivityNameHandler(null));
    }
  };

  const authorNameHandler = (e) => {
    let value = e.target.value.trim();

    if (value.length >= 3) {
      dispatch(addAuthorNameHandler(value));
      dispatch(addPageHandler(1));
    } else {
      dispatch(addAuthorNameHandler(null));
    }
  };

  return (
    <Wrapper>
      <InputsWrapper>
        <Label>
          <Title>{COMP_FILTER_BAR.nameTitle}</Title>
          <Input
            defaultValue={state?.activityName}
            placeholder={COMP_FILTER_BAR.namePlaceholder}
            name="activityName"
            type="text"
            onChange={activityNameHandler}
          />
        </Label>

        <Label>
          <Title>{COMP_FILTER_BAR.authorNameTitle}</Title>
          <Input
            defaultValue={state?.authorName}
            placeholder={COMP_FILTER_BAR.authorNamePlaceholder}
            name="authorName"
            type="text"
            onChange={authorNameHandler}
          />
        </Label>
      </InputsWrapper>
      <SubContainer>
        <DaysWrapper>
          <DayPicker
            title={COMP_FILTER_BAR.from}
            dateCallback={addStartDateHandler}
            pageCallback={addPageHandler}
            state={state?.startDate}
            dateFiltration={state?.dateFiltration}
          />
          {!isTablet && (
            <ToggleWrapper>
              <DateToggle
                dateFiltrationCallback={setDateFiltration}
                pageCallback={addPageHandler}
                dateFiltration={state?.dateFiltration}
              />
            </ToggleWrapper>
          )}
          <DayPicker
            title={COMP_FILTER_BAR.to}
            dateCallback={addEndDateHandler}
            pageCallback={addPageHandler}
            plusDays={7}
            state={state?.endDate}
            dateFiltration={state?.dateFiltration}
          />
          {isTablet && (
            <ToggleWrapper>
              <DateToggle
                dateFiltrationCallback={setDateFiltration}
                pageCallback={addPageHandler}
                dateFiltration={state?.dateFiltration}
              />
            </ToggleWrapper>
          )}
        </DaysWrapper>
      </SubContainer>
      <DivisionWrapper>
        <DivisionBar
          addDivisionCallback={addDivisionHandler}
          pageCallback={addPageHandler}
          state={state?.divisions}
        />
      </DivisionWrapper>
      <Error
        initial={{ opacity: 0 }}
        animate={{
          opacity: error ? 1 : 0,
        }}
        transition={{ type: "spring", duration: error ? 2 : 0 }}
      >
        {error}
      </Error>
    </Wrapper>
  );
};

const Wrapper = styled.div.attrs({
  className: "bg-slate-100 border-slate-300",
})`
  ${tw`border-b-2 pt-20 3xl:pt-32 flex flex-col shadow-md font-light`}
`;

const SubContainer = styled.div`
  ${tw`flex flex-1 justify-center`}
`;

const DivisionWrapper = styled.div`
  ${tw`flex justify-center`}
`;

const DaysWrapper = styled.div`
  ${tw`w-[75%] flex flex-col lg:flex-row lg:justify-between`}
`;

const InputsWrapper = styled.div`
  ${tw`flex-1 flex items-center justify-center flex-col`}
`;

const Title = styled.span`
  ${tw`font-semibold w-full text-center lg:w-auto lg:text-left text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all`}
`;

const Label = styled.label`
  ${tw`w-[75%] flex flex-col py-2 3xl:py-6 4xl:py-8`}
`;

const Input = styled.input.attrs({ className: "border-slate-300" })`
  ${tw`rounded-xl font-light outline-none focus:shadow-lg focus:border-purple-400 border-2 3xl:border-4 p-1 3xl:p-2 px-2 3xl:px-4 my-4 3xl:my-6 text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-2xl 4xl:text-3xl transition-all`}
`;

const Error = styled(motion.span)`
  ${tw`text-red-400 w-full text-center text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-2xl 4xl:text-3xl transition-all`}
`;

const ToggleWrapper = styled.div`
  ${tw`flex justify-center items-end`}
`;
export default FilterBar;
