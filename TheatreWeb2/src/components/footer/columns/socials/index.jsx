import React from "react";
import { motion } from "framer-motion";
import styled from "styled-components";
import tw from "twin.macro";

import Twitter from "../../../../assets/twitter";
import Facebook from "../../../../assets/facebook";
import Instagram from "../../../../assets/instagram";

import { COMP_FOOTER } from "../../../../config/texts";
import {
  FACEBOOK,
  TWITTER,
  INSTAGRAM,
} from "../../../../config/navigation/paths";

const Socials = () => {
  return (
    <Column>
      <Container>
        <Title>{COMP_FOOTER.aboutUs.title}</Title>
        <Body>
          <SocialTab
            whileHover={{
              scale: 0.9,
              transition: { type: "spring", duration: 0.1 },
            }}
            href={FACEBOOK}
            target="_blank"
            rel="noopener noreferrer"
          >
            <Facebook />
            {COMP_FOOTER.aboutUs.fbLinkName}
          </SocialTab>
          <SocialTab
            whileHover={{
              scale: 0.9,
              transition: { type: "spring", duration: 0.1 },
            }}
            href={TWITTER}
            target="_blank"
            rel="noopener noreferrer"
          >
            <Twitter />
            {COMP_FOOTER.aboutUs.twLinkName}
          </SocialTab>
          <SocialTab
            href={INSTAGRAM}
            target="_blank"
            rel="noopener noreferrer"
            whileHover={{
              scale: 0.9,
              transition: { type: "spring", duration: 0.1 },
            }}
          >
            <Instagram />
            {COMP_FOOTER.aboutUs.inLinkName}
          </SocialTab>
        </Body>
      </Container>
    </Column>
  );
};

const Column = styled.div`
  ${tw`flex-1 flex flex-col mx-[10%] lg:mx-0`}
`;

const Container = styled.div`
  ${tw`min-w-[65%] lg:w-full lg:pr-6`}
`;

const Title = styled.h3.attrs({ className: "w-fit" })`
  ${tw`pr-8 lg:pt-3 ml-1 mt-3 border-purple-500 font-semibold text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-3xl 4xl:text-4xl transition-all border-b-2`}
`;

const Body = styled.div`
  ${tw`flex pt-4 flex-wrap justify-between`}
`;

const SocialTab = styled(motion.a)`
  ${tw`flex flex-col justify-center items-center my-2 cursor-pointer mr-1 text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-2xl 4xl:text-3xl transition-all`}
`;

export default Socials;
