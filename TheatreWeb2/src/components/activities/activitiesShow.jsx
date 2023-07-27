import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { trackWindowScroll } from "react-lazy-load-image-component";

import useImagePreviews from "../../hooks/useImagePreviews";

import ActivityCard from "../activitiesCard";
import LastBox from "./lastBox";
import NoResultBox from "./noResultBox";

const ActivitiesShow = ({
  previews,
  lastBox = false,
  showActionToolbar = false,
  mobileMode,
  scrollPosition,
}) => {
  const images = useImagePreviews(previews);
  return (
    <MainContainer>
      {previews.length ? (
        <>
          {previews.map((activity, index) => {
            return (
              <CardWrapper
                key={index}
                $rowSpan={previews.length >= 3 && index % 4 === 1}
              >
                <ActivityCard
                  showActionToolbar={showActionToolbar}
                  imgSrc={images[index] && images[index].imgUrl}
                  activity={activity}
                  mobileMode={mobileMode}
                  scrollPosition={scrollPosition}
                />
              </CardWrapper>
            );
          })}
          {lastBox && <LastBox />}
        </>
      ) : (
        <NoResultBox />
      )}
    </MainContainer>
  );
};

const MainContainer = styled.div`
  ${tw`grid gap-4 3xl:gap-6 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 mx-4 my-8 3xl:mx-6 3xl:my-12 `}
`;

const CardWrapper = styled.div(({ $rowSpan }) => [
  tw` min-h-[400px] xl:min-h-[500px] 3xl:min-h-[800px] border-4 border-gray-300 rounded-md overflow-hidden transition-all cursor-pointer opacity-90 hover:(border-purple-500 border-8 opacity-100 shadow-2xl)`,
  $rowSpan && tw`row-span-2`,
]);

export default trackWindowScroll(ActivitiesShow);
