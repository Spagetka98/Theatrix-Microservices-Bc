import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import FilterBar from "../../components/filterBar";

import ClockSpinner from "../../components/clockSpinner";
import ErrorWarning from "../../components/errorWarning";
import MobileToggle from "../../components//mobileToggle";

import useActivities from "./service";
import PaginationBox from "./paginationBox";
import ActivitiesShow from "./activitiesShow";

const Activities = ({
  showActionToolbar,
  lastBox,
  state,
  addActivityNameHandler,
  addAuthorNameHandler,
  addStartDateHandler,
  addEndDateHandler,
  addDivisionHandler,
  addPageHandler,
  addSizeOfPageHandler,
  setDateFiltration,
}) => {
  const {
    paramsError,
    loading,
    error,
    data,
    mobileMode,
    pageHandler,
    mobileModeHandler,
  } = useActivities(state, lastBox, addSizeOfPageHandler, addPageHandler);

  return (
    <Wrapper>
      <FilterBar
        error={paramsError}
        state={state}
        addActivityNameHandler={addActivityNameHandler}
        addAuthorNameHandler={addAuthorNameHandler}
        addStartDateHandler={addStartDateHandler}
        addEndDateHandler={addEndDateHandler}
        addDivisionHandler={addDivisionHandler}
        addPageHandler={addPageHandler}
        setDateFiltration={setDateFiltration}
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
          <ErrorWarning hideImg hideTitle style={{ "--minHeight": "0" }} />
        </Container>
      ) : (
        <>
          <PaginationBox
            activitiesLength={data?.previews?.length}
            count={data?.totalPages}
            currentPage={state?.page + 1}
            loading={loading}
            pageHandler={pageHandler}
          />

          {data?.previews && (
            <ActivitiesShow
              previews={data.previews}
              lastBox={lastBox}
              showActionToolbar={showActionToolbar}
              mobileMode={mobileMode}
            />
          )}
        </>
      )}
      <MobileToggle status={mobileMode} mobileModeHandler={mobileModeHandler} />
    </Wrapper>
  );
};

const Wrapper = styled.section`
  ${tw`min-h-screen`}
`;

const Container = styled.div`
  ${tw`flex justify-center items-center min-h-[300px]`}
`;

export default Activities;
