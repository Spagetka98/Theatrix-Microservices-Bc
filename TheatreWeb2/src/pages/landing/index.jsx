import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Hero from "../../components/hero";
import AppDescription from "../../components/appDescription";
import BestActivities from "../../components/bestActivities";

const LandingPage = () => {
  return (
    <Wrapper>
      <Hero />
      <Divider />
      <AppDescription />
      <Divider />
      <BestActivities />
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex flex-col md:relative`}
`;

const Divider = styled.div`
  ${tw`min-h-[4vw] z-20 max-h-14 shadow-md bg-gradient-to-r from-[#FFAFBD] to-[#F4E2D8]`}
`;
export default LandingPage;
