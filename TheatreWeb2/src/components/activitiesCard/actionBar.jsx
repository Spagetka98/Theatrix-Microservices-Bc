import React, { useState } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { motion } from "framer-motion";
import { useMediaQuery } from "react-responsive";

import ArrowDown from "../../assets/arrowDown";

import { FULL_HD } from "../../config/sizes";

import ActionToolbar from "../actionToolbar";

const ActionBar = ({ activity }) => {
  const [openToolbar, setOpenToolbar] = useState(false);
  const isFullHD = useMediaQuery({
    query: FULL_HD,
  });

  const toggleToolBar = () => {
    setOpenToolbar(!openToolbar);
  };
  return (
    <Wrapper
      initial={{ height: "0%" }}
      animate={{ height: openToolbar ? "65%" : "0%" }}
      transition={{ type: "spring", duration: 1 }}
    >
      <Body
        initial={{ opacity: 0 }}
        animate={{ opacity: openToolbar ? "1" : "0" }}
        transition={{ type: "spring", duration: 1.5 }}
      >
        {openToolbar && (
          <ActionToolbar
            id={activity.idActivity}
            likedByUser={activity.likedByUser}
            dislikedByUser={activity.dislikedByUser}
            ratedByUser={activity.ratedByUser}
            totalLiked={activity.totalLiked}
            totalDisliked={activity.totalDisliked}
            totalRated={activity.totalRated}
          />
        )}
      </Body>

      <Button>
        <Arrow
          onClick={toggleToolBar}
          initial={{ rotate: 0, borderRadius: "0% 0% 90% 90%" }}
          animate={{
            rotate: openToolbar ? 180 : 0,
            borderRadius: openToolbar ? "90% 90% 0% 0%" : "0% 0% 90% 90%",
          }}
          transition={{ type: "spring", duration: 1.5, delay: 0.8 }}
        >
          <ArrowDown
            style={{
              "--height": isFullHD ? "10%" : "100%",
              "--width": isFullHD ? "10%" : "100%",
            }}
          />
        </Arrow>
      </Button>
    </Wrapper>
  );
};

const Wrapper = styled(motion.div)`
  ${tw`absolute left-0 top-0 w-full backdrop-blur-xl bg-white/80 z-20`}
`;

const Body = styled(motion.div)`
  ${tw`h-full opacity-0`}
`;

const Button = styled.div`
  ${tw`flex flex-col items-center justify-center z-20`}
`;

const Arrow = styled(motion.div).attrs({ className: "w-fit" })`
  ${tw`flex justify-center items-center py-1 3xl:p-3 backdrop-blur-xl bg-white/80`}
`;

export default ActionBar;
