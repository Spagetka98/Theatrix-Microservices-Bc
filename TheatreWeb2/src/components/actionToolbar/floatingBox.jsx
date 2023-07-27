import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { useMediaQuery } from "react-responsive";

import ClockSpinner from "../clockSpinner";

import { FULL_HD } from "../../config/sizes";

const FloatingBox = ({ loading = false, msg }) => {
  const isFullHD = useMediaQuery({
    query: FULL_HD,
  });
  return (
    <Wrapper>
      <ClockSpinner
        loading={loading}
        style={{ "--minHeight": "0" }}
        size={isFullHD ? 40 : 70}
      />
      {msg && <Message>{msg}</Message>}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`w-full text-center absolute bottom-0 left-0 pb-1`}
`;

const Message = styled.span`
  ${tw`text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all text-red-500 italic`}
`;
export default FloatingBox;
