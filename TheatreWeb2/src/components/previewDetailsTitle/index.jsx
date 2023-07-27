import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import moment from "moment";

import ClockSpinner from "../clockSpinner";

import { COMP_PREVIEWS_DETAILS_TITLE } from "../../config/texts";

const PreviewDetailsTitle = ({ loading, error, data, review }) => {
  return (
    <div>
      {loading ? (
        <ClockSpinner
          loading={loading}
          size={50}
          style={{ "--minHeight": "0" }}
        />
      ) : error ? (
        <Error>{COMP_PREVIEWS_DETAILS_TITLE.errorText}</Error>
      ) : (
        data &&
        data
          .filter((detail) => detail.idActivity === review.activityID)
          .map((filteredDetail, indexDetail) => (
            <Container key={indexDetail}>
              <Title>{filteredDetail.name}</Title>

              {moment(filteredDetail.startDate).format("DD.MM.yyyy HH:mm")}
            </Container>
          ))
      )}
    </div>
  );
};

const Container = styled.div`
  ${tw`flex items-center flex-col font-light`}
`;

const Title = styled.div`
  ${tw`flex items-center text-center px-2 py-2 font-bold break-all`}
`;

const Error = styled.span`
  ${tw`font-bold pt-3 px-2 break-all`}
`;

export default PreviewDetailsTitle;
