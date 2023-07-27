import React, { useReducer } from "react";
import styled from "styled-components";
import tw from "twin.macro";

import StarsPicker from "./starsPicker";

import useRatingForm from "./services";
import Actions from "./formActions";

import { COMP_SIDE_RATING_BOX_RATING_FORM } from "../../config/texts";

import ClockSpinner from "../clockSpinner";

const initialState = {
  userRated: false,
  loading: false,
  userRating: null,
  ratingTitle: null,
  ratingText: null,
};
const reducer = (state, action) => {
  switch (action.type) {
    case Actions.INIT:
      return { ...state, ...action.payload };
    case Actions.DELETE:
      return {
        ...state,
        loading: false,
        userRated: false,
        userRating: null,
        ratingTitle: null,
        ratingText: null,
      };
    case Actions.SET_USER_RATING:
      return {
        ...state,
        userRating: action.payload.userRating,
        ratingTitle: action.payload.ratingTitle,
        ratingText: action.payload.ratingText,
      };
    case Actions.SET_LOADING:
      return { ...state, loading: action.payload };
    case Actions.SET_USER_RATED:
      return { ...state, userRated: action.payload };
    case Actions.SET_MESSAGE:
      return {
        ...state,
        isError: action.payload.isError,
        message: action.payload.message,
      };
    case Actions.CLEAR_MESSAGE:
      return {
        ...state,
        isError: false,
        message: null,
      };
    default:
      throw new Error("Unexpected action in sideRatingBox - ratingForm");
  }
};
const RatingForm = ({ id, realoadHandler, openHandler }) => {
  const [formState, dispatch] = useReducer(reducer, initialState);
  const {
    ratingRef,
    titleRef,
    textAreaRef,
    addHandler,
    updateHandler,
    removeHandler,
  } = useRatingForm(id, dispatch, realoadHandler);

  return (
    <Form>
      {formState.loading ? (
        <SpinnerContainer>
          <ClockSpinner loading={formState.loading} />
        </SpinnerContainer>
      ) : (
        <Wrapper>
          <UpperContainer>
            <LeftContainer>
              <StarsPicker
                ref={ratingRef}
                disabled={formState?.loading}
                currentRating={formState?.userRating ? formState.userRating : 1}
              />
            </LeftContainer>
            <RightContainer>
              <Input
                ref={titleRef}
                disabled={formState?.loading}
                defaultValue={
                  formState?.ratingTitle ? formState.ratingTitle : ""
                }
                type="text"
                placeholder={COMP_SIDE_RATING_BOX_RATING_FORM.titlePlaceholder}
              />
              <TextArea
                ref={textAreaRef}
                disabled={formState?.loading}
                defaultValue={
                  formState?.ratingText ? formState.ratingText : undefined
                }
                placeholder={COMP_SIDE_RATING_BOX_RATING_FORM.textPlaceholder}
                maxLength="5000"
              ></TextArea>
            </RightContainer>
          </UpperContainer>
          <LowerContainer>
            <MessageBox isError={formState?.isError}>
              {formState?.message}
            </MessageBox>
            <ButtonsContainer>
              {formState.userRated ? (
                <div>
                  <DeleteButton onClick={removeHandler}>
                    {COMP_SIDE_RATING_BOX_RATING_FORM.delete}
                  </DeleteButton>
                  <ChangeButton onClick={updateHandler}>
                    {COMP_SIDE_RATING_BOX_RATING_FORM.change}
                  </ChangeButton>
                </div>
              ) : (
                <AddButton onClick={addHandler}>
                  {COMP_SIDE_RATING_BOX_RATING_FORM.create}
                </AddButton>
              )}
              <CloseButton onClick={() => openHandler()}>
                {COMP_SIDE_RATING_BOX_RATING_FORM.close}
              </CloseButton>
            </ButtonsContainer>
          </LowerContainer>
        </Wrapper>
      )}
    </Form>
  );
};

const Form = styled.form`
  ${tw`flex h-full w-full text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all overflow-auto`}
`;

const LeftContainer = styled.div`
  ${tw`flex flex-col justify-center items-center w-[20%] lg:w-[15%] overflow-y-auto`}
`;

const RightContainer = styled.div`
  ${tw`flex flex-col m-2 w-[80%] lg:w-[85%]`}
`;

const Input = styled.input`
  ${tw`flex-1 border-2 rounded-xl px-2`}
`;

const TextArea = styled.textarea.attrs({ className: "flex-[4_4_0%]" })`
  ${tw`resize-none border-2 rounded-xl mt-2 p-2`}
`;

const LowerContainer = styled.div`
  ${tw`flex flex-col mb-1 h-[20%]`}
`;

const MessageBox = styled.div(({ isError }) => [
  tw`flex-1 flex justify-center items-center`,
  isError ? tw`text-red-400` : tw`text-green-400`,
]);

const ButtonsContainer = styled.div`
  ${tw`flex flex-1 justify-between items-center mx-1`}
`;

const Button = styled.button`
  ${tw`p-2 3xl:p-3 rounded-xl shadow-xl cursor-pointer font-medium mx-2`}
`;

const DeleteButton = styled(Button)`
  ${tw`bg-red-400`}
`;
const ChangeButton = styled(Button).attrs({ className: "bg-orange-400" })``;
const AddButton = styled(Button)`
  ${tw`bg-green-400`}
`;
const CloseButton = styled(Button)`
  ${tw`bg-red-400`}
`;

const SpinnerContainer = styled.div`
  ${tw`flex flex-1 justify-center items-center`}
`;
const Wrapper = styled.div`
  ${tw`flex flex-col flex-1`}
`;

const UpperContainer = styled.div`
  ${tw`flex h-[80%]`}
`;
export default RatingForm;
