import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import ExclamationMark from "../../assets/exclamationMark";

import Colors from "../../config/colors/iconColors";

const TextHint = ({ text }) => {
  return (
    <Wrapper>
      <ExclamationMark
        fillColor={Colors.lightOrange}
        strokeColor={Colors.darkOrange}
      />
      <Text>{text}</Text>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex py-2 3xl:py-4 items-center`}
`;

const Text = styled.p`
  ${tw`pl-4 text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-2xl 4xl:text-3xl transition-all`}
`;
export default TextHint;
