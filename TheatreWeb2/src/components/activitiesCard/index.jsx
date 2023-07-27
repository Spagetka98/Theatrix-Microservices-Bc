import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { Link } from "react-router-dom";
import _ from "lodash";
import { LazyLoadImage } from "react-lazy-load-image-component";

import { DETAILS } from "../../config/navigation/paths";
import colors from "../../config/colors/activityCard";

import useTimer from "../../hooks/useTimer";

import ActionBar from "./actionBar";
import Description from "./description";

const ActivitiesCard = ({
  showActionToolbar = true,
  imgSrc,
  activity,
  mobileMode,
  scrollPosition,
}) => {
  const available = useTimer(activity.available, activity.milliSecLeft);
  return (
    <Wrapper>
      {showActionToolbar && available && <ActionBar activity={activity} />}

      <Link to={DETAILS + "/" + activity.idActivity}>
        {mobileMode ? (
          <GradientBody
            style={{
              "--lightColor": _.sample(colors),
              "--darkColor": _.sample(colors),
            }}
          ></GradientBody>
        ) : (
          <Img
            alt="activityPreview"
            src={imgSrc}
            scrollPosition={scrollPosition}
            effect="blur"
          />
        )}
        <Description activity={activity} available={available} />
      </Link>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`relative h-full`}
`;

const Img = styled(LazyLoadImage)`
  ${tw`w-full h-full object-cover`}
`;

const GradientBody = styled.div`
  ${tw`bg-gradient-to-r from-[var(--lightColor,lightcoral)] to-[var(--darkColor,darksalmon)] flex justify-center items-center h-full`}
`;
export default ActivitiesCard;
