import React from "react";
import { motion } from "framer-motion";
import styled from "styled-components";
import tw from "twin.macro";
import { Link } from "react-router-dom";

import { DETAILS } from "../../config/navigation/paths";
import { COMP_SLIDER_CARD } from "../../config/texts";

const LinkSection = ({ url, id }) => {
  return (
    <Wrapper
      initial={{ opacity: 0, x: 100 }}
      animate={{ opacity: 1, x: 0 }}
      transition={{ type: "spring", duration: 2.3, delay: 1.6 }}
    >
      <Title>{COMP_SLIDER_CARD.anotherInformation}</Title>
      <Container>
        {url && (
          <Box>
            {COMP_SLIDER_CARD.officialWeb}
            <LinkBlue
              whileHover={{ scale: 0.96 }}
              href={"https://" + url}
              target="_blank"
              rel="noopener noreferrer"
            >
              {COMP_SLIDER_CARD.link}
            </LinkBlue>
          </Box>
        )}
        <Box>
          {COMP_SLIDER_CARD.ourWeb}

          <LinkOur whileHover={{ scale: 0.96 }}>
            <Link to={DETAILS + "/" + id}>{COMP_SLIDER_CARD.ourTitle}</Link>
          </LinkOur>
        </Box>
      </Container>
    </Wrapper>
  );
};

const Wrapper = styled(motion.div)`
  ${tw`ml-2 mb-4 py-2 shadow-md z-10 sm:ml-0 w-full flex bg-red-50 flex-col justify-center text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all font-extralight rounded-l-xl border-l-2 border-t-2 border-b-2  3xl:border-l-4  3xl:border-t-4 3xl:border-b-4 border-pink-200 `}
`;

const Title = styled.h3`
  ${tw`flex-1 text-center font-semibold`}
`;

const Container = styled.div`
  ${tw`flex flex-1`}
`;

const Box = styled.span`
  ${tw`flex-1 flex justify-center items-center py-2`}
`;

const LinkBlue = styled(motion.a)`
  ${tw`text-blue-500 ml-1 lg:hover:opacity-60`}
`;

const LinkOur = styled(motion.span)`
  ${tw`text-blue-500 ml-1 lg:hover:opacity-60`}
`;

export default LinkSection;
