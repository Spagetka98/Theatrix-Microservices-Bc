import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

const TextHint = ({ title, text }) => {
  return (
    <Wrapper>
      <Title>{title}</Title>
      <Text>{text}</Text>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex py-2 3xl:py-6`}
`;

const Title = styled.div.attrs({
  className: "flex-[2_2_0%]",
})`
  ${tw`font-bold `}
`;

const Text = styled.div.attrs({
  className: "flex-[6_6_0%]",
})`
  ${tw`flex items-center`}
`;
export default TextHint;
