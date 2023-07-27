import React from "react";
import { motion } from "framer-motion";
import styled from "styled-components";
import tw from "twin.macro";

import Counter from "./counter";

import ThumbUp from "../../assets/thumbUp";
import ThumbDown from "../../assets/thumbDown";
import Chat from "../../assets/chat";
import SplashesLinks from "../../assets/freepik/splashes";

import Colors from "../../config/colors/iconColors";

const CounterSection = ({ totalLiked, totalRatings, totalDisliked }) => {
  return (
    <Wrapper
      initial={{ opacity: 0, y: 100 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ type: "spring", duration: 2.3, delay: 1.6 }}
    >
      <Counter backUrl={SplashesLinks[0].imgUrl} count={totalLiked}>
        <ThumbUp fillColor={Colors.darkGreen} />
      </Counter>

      <Counter backUrl={SplashesLinks[1].imgUrl} count={totalRatings}>
        <Chat fillColor={Colors.darkYellow} />
      </Counter>

      <Counter backUrl={SplashesLinks[2].imgUrl} count={totalDisliked}>
        <ThumbDown fillColor={Colors.darkRed} />
      </Counter>
    </Wrapper>
  );
};
const Wrapper = styled(motion.div)`
  ${tw`md:mr-2 flex items-start my-4 py-10`}
`;

export default CounterSection;
