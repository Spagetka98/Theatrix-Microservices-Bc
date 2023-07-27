import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Rose from "../../assets/freepik/rose";

import ActionToolbar from "../../components/actionToolbar";

const Toolbar = ({ id, data, available }) => {
  return (
    <Wrapper>
      <Img src={Rose[0].imgUrl} alt="detailImg" />
      {available && (
        <ToolbarContainer>
          <ActionToolbar
            id={id}
            likedByUser={data?.likedByUser}
            dislikedByUser={data?.dislikedByUser}
            ratedByUser={data?.ratedByUser}
            totalLiked={data?.totalLiked}
            totalDisliked={data?.totalDisliked}
            totalRated={data?.totalRated}
            hideRatingBox
          />
        </ToolbarContainer>
      )}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex flex-col justify-between`}
`;

const Img = styled.img`
  ${tw`object-cover lg:h-full`}
`;

const ToolbarContainer = styled.div`
  ${tw`mx-2 border-2 xl:border-4 shadow-md rounded-3xl overflow-hidden hover:(border-purple-500 border-2 xl:border-4 shadow-2xl) transition-all`}
`;

export default Toolbar;
