import React, { useState, useEffect } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { useParams } from "react-router-dom";
import Pagination from "@mui/material/Pagination";

import withAuth from "../../components/withAuth";
import UserRatingPreviews from "../../components/userRatingPreviews";
import ClockSpinner from "../../components/clockSpinner";

import useAxios from "../../hooks/useAxios";

import { PAGES_USER_REVIEWS } from "../../config/texts";
import { GET_USER_RATING_REVIEWS } from "../../config/axios/endpoints";

const UserReviews = () => {
  const { username } = useParams();
  const [currentPage, setCurrentPage] = useState(0);
  const [data, error, loading, setConfigParams] = useAxios();

  useEffect(() => {
    const configData = {
      username: username,
      currentPage: currentPage,
      sizeOfPage: 10,
    };

    setConfigParams(
      GET_USER_RATING_REVIEWS.url,
      GET_USER_RATING_REVIEWS.method,
      configData
    );
  }, [username, currentPage, setConfigParams]);

  const pageHandler = (event, value) => {
    setCurrentPage(value - 1);
  };

  return (
    <Wrapper>
      <UpperBox>
        <Title>{PAGES_USER_REVIEWS.title}</Title>
        <Username>{username}</Username>
      </UpperBox>

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
          <ResultTitle>{PAGES_USER_REVIEWS.errorResult}</ResultTitle>
        </Container>
      ) : (
        data && (
          <>
            {data.previews?.length > 0 && (
              <PagginationWrapper>
                <Pagination
                  count={data?.totalPages}
                  page={currentPage + 1}
                  color="secondary"
                  disabled={loading}
                  size="large"
                  onChange={pageHandler}
                />
              </PagginationWrapper>
            )}

            {data.previews?.length > 0 ? (
              <UserRatingPreviews previews={data?.previews} />
            ) : (
              <Container>
                <ResultTitle>{PAGES_USER_REVIEWS.emptyResult}</ResultTitle>
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

const UpperBox = styled.div.attrs({
  className: "bg-slate-100 border-slate-300",
})`
  ${tw`items-center w-full border-b-2 pt-20 pb-4 3xl:pt-32 flex flex-col shadow-md font-light `}
`;

const Title = styled.div`
  ${tw`text-base md:text-lg lg:text-xl 2xl:text-3xl 3xl:text-4xl transition-all`}
`;

const ResultTitle = styled.span.attrs({
  className:
    "bg-clip-text text-transparent bg-gradient-to-r from-pink-500 to-violet-500",
})`
  ${tw`font-extrabold pt-3 uppercase px-2 text-base lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-5xl transition-all`}
`;

const Username = styled.div`
  ${tw`text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all italic lg:pt-2`}
`;

const Container = styled.div`
  ${tw`flex justify-center items-center min-h-[300px]`}
`;

const PagginationWrapper = styled.div`
  ${tw`w-full flex justify-center items-center p-4`}
`;

export default withAuth(UserReviews);
