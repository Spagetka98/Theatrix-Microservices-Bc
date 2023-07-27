import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Star from "../../assets/star";

import Actions from "./actions";

const RatingButton = ({ color, rating, dispatch, active }) => {
  const clickHandler = () => {
    dispatch({ type: Actions.SET_RATINGS, payload: rating });
    dispatch({ type: Actions.SET_PAGE, payload: 1 });
  };
  return (
    <Wrapper>
      <Button
        onClick={clickHandler}
        style={{ "--bgColor": active ? color : "#f4f4f4" }}
      >
        <Star
          fillColor={"#f1c92b"}
          style={{
            "--minHeight": "2rem",
            "--minWidth": "2rem",
            "--width": "100%",
            "--height": "100%",
          }}
        />
        {rating}
      </Button>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex-1 flex sm:justify-center`}
`;

const Button = styled.button.attrs({ className: "bg-slate-50" })`
  ${tw`border-2 xl:border-4 4xl:border-8 border-gray-300  hover:border-purple-500 shadow-lg bg-[var(--bgColor,"#b7b7b7")] p-4 sm:px-8 3xl:p-8 m-4 rounded-lg flex justify-center items-center flex-col font-extrabold min-w-[50px]`}
`;

export default RatingButton;
