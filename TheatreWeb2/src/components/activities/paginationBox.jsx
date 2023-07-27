import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import Pagination from "@mui/material/Pagination";

const PaginationBox = ({
  activitiesLength = 0,
  count = 0,
  currentPage = 0,
  loading = false,
  pageHandler,
}) => {
  if (activitiesLength === 0) return null;

  return (
    <Wrapper>
      <Pagination
        count={count}
        page={currentPage}
        color="secondary"
        disabled={loading}
        size="large"
        onChange={pageHandler}
      />
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`w-full flex justify-center items-center pt-7`}
`;

export default PaginationBox;
