import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import { COMP_RATING_BAR_DATE_TOGGLE } from "../../config/texts";

import Actions from "./actions";

const RatingDateToggle = ({ dateFiltration, dispatch }) => {
  const clickHandler = () => {
    dispatch({
      type: Actions.SET_DATE_FILTRATION,
      payload: !dateFiltration,
    });
    dispatch({ type: Actions.SET_PAGE, payload: 1 });
  };
  return (
    <Wrapper>
      <Button
        onClick={clickHandler}
        style={{ "--bgColor": dateFiltration ? "#a7ff71bc" : "#f4f4f4" }}
      >
        {COMP_RATING_BAR_DATE_TOGGLE.title}
      </Button>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex`}
`;

const Button = styled.button.attrs({ className: "bg-slate-50" })`
  ${tw`p-2 border-2 xl:border-4 4xl:border-8 border-gray-300  hover:border-purple-500 shadow-lg bg-[var(--bgColor,"#b7b7b7")]  rounded-lg  font-semibold min-w-[50px]`}
`;

export default RatingDateToggle;
