import React from "react";
import { motion } from "framer-motion";
import styled from "styled-components";
import tw from "twin.macro";
import { Link } from "react-router-dom";

import { COMP_APP_DESCRIPTION } from "../../config/texts";
import { LOGIN } from "../../config/navigation/paths";

const LinkBox = () => {
  return (
    <>
      <Title>{COMP_APP_DESCRIPTION.encouragingTitle}</Title>
      <Box whileHover={{ scale: 0.96 }}>
        <Link to={LOGIN}>{COMP_APP_DESCRIPTION.encouragingTitleLink}</Link>
      </Box>
    </>
  );
};

const Title = styled.p`
  ${tw`font-bold pt-4 3xl:pt-6 text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all`}
`;

const Box = styled(motion.p).attrs({ className: "w-fit" })`
  ${tw`pt-2 3xl:pt-4 cursor-pointer text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-2xl 4xl:text-4xl transition-all text-blue-500 lg:hover:opacity-60`}
`;
export default LinkBox;
