import React, { useReducer } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { useParams } from "react-router-dom";
import { xor } from "lodash";
import moment from "moment";
import Pagination from "@mui/material/Pagination";

import withAuth from "../../components/withAuth";
import RatingBar from "../../components/ratingBar";
import Actions from "../../components/ratingBar/actions";
import RatingPreviews from "../../components/ratingPreviews";
import ClockSpinner from "../../components/clockSpinner";

import { PAGES_ACTIVITY_RATINGS } from "../../config/texts";

import useRatings from "./service";

const initialState = {
  page: 0,
  sizeOfPage: 20,
  ratings: [1, 2, 3, 4, 5],
  startDate: null,
  endDate: null,
  dateFiltration: false,
  errorMessage: null,
};
const reducer = (state, action) => {
  switch (action.type) {
    case Actions.SET_DATE_FILTRATION:
      return { ...state, dateFiltration: action.payload };
    case Actions.SET_START_DATE:
      return {
        ...state,
        startDate: moment(new Date(action.payload)).format("DD-MM-YYYY"),
      };
    case Actions.SET_END_DATE:
      return {
        ...state,
        endDate: moment(new Date(action.payload)).format("DD-MM-YYYY"),
      };
    case Actions.SET_RATINGS:
      return { ...state, ratings: xor(state.ratings, [action.payload]) };
    case Actions.SET_PAGE:
      return { ...state, page: action.payload - 1 };
    case Actions.SET_SIZE_OF_PAGE:
      return { ...state, sizeOfPage: action.payload };
    default:
      throw new Error("Unexpected action");
  }
};
const ActivityRatings = () => {
  const { id } = useParams();
  const [ratingState, dispatch] = useReducer(reducer, initialState);
  const { loading, data, error, errorMessage, pageHandler } = useRatings(
    id,
    ratingState,
    dispatch
  );

  return (
    <Wrapper>
      <RatingBar
        state={ratingState}
        dispatch={dispatch}
        errorMessage={errorMessage}
      />
      {loading ? (
        <Container>
          <ClockSpinner
            loading={loading}
            size={100}
            style={{ "--minHeight": "0" }}
          />
        </Container>
      ) : error ? (
        <Container>
          <Title>{PAGES_ACTIVITY_RATINGS.errorResult}</Title>
        </Container>
      ) : (
        data && (
          <>
            {data.previews?.length > 0 && (
              <PagginationWrapper>
                <Pagination
                  count={data?.totalPages}
                  page={ratingState?.page + 1}
                  color="secondary"
                  disabled={loading}
                  size="large"
                  onChange={pageHandler}
                />
              </PagginationWrapper>
            )}

            {data.previews?.length > 0 ? (
              <RatingPreviews data={data?.previews} />
            ) : (
              <Container>
                <Title>{PAGES_ACTIVITY_RATINGS.emptyResult}</Title>
              </Container>
            )}
          </>
        )
      )}
    </Wrapper>
  );
};

const Wrapper = styled.section`
  ${tw`min-h-screen`}
`;

const Container = styled.div`
  ${tw`flex justify-center items-center min-h-[300px]`}
`;

const PagginationWrapper = styled.div`
  ${tw`w-full flex justify-center items-center p-4`}
`;

const Title = styled.span.attrs({
  className:
    "bg-clip-text text-transparent bg-gradient-to-r from-pink-500 to-violet-500",
})`
  ${tw`font-extrabold pt-3 uppercase px-2 text-base lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-5xl transition-all`}
`;
export default withAuth(ActivityRatings);
