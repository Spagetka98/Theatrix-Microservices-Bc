import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import { COMP_ACTIVITIES_NO_RESULT_BOX } from "../../config/texts";

const NoResultBox = () => {
  return (
    <Wrapper>
      <Title>{COMP_ACTIVITIES_NO_RESULT_BOX.title}</Title>
      <Text>{COMP_ACTIVITIES_NO_RESULT_BOX.text}</Text>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`border-4 flex flex-col rounded-md bg-gray-100 font-extralight text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all col-span-3`}
`;

const Title = styled.span.attrs({
  className:
    "bg-clip-text text-transparent bg-gradient-to-r from-pink-500 to-violet-500",
})`
  ${tw`font-extrabold pt-3 uppercase px-2`}
`;

const Text = styled.span`
  ${tw`p-2`}
`;
export default NoResultBox;
