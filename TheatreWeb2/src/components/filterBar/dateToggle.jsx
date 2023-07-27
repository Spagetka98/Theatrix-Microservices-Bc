import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { useDispatch } from "react-redux";

import { COMP_FILTER_BAR_DATE_TOGGLE } from "../../config/texts";

const DateToggle = ({
  dateFiltrationCallback,
  pageCallback,
  dateFiltration,
}) => {
  const dispatch = useDispatch();
  const clickHandler = () => {
    dispatch(dateFiltrationCallback(!dateFiltration));
    dispatch(pageCallback(1));
  };
  return (
    <Wrapper>
      <Button
        onClick={clickHandler}
        style={{ "--bgColor": dateFiltration ? "#a7ff71bc" : "#f4f4f4" }}
      >
        {COMP_FILTER_BAR_DATE_TOGGLE.title}
      </Button>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex mx-4 mt-2 lg:mt-0 `}
`;

const Button = styled.button.attrs({ className: "bg-slate-50" })`
  ${tw`p-2 border-2 xl:border-4 4xl:border-8 border-gray-300 hover:border-purple-500 shadow-lg bg-[var(--bgColor,"#b7b7b7")] rounded-lg font-semibold min-w-[50px]`}
`;

export default DateToggle;
