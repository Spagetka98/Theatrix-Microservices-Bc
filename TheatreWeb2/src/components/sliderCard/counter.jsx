import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

const Counter = ({ backUrl, count, children }) => {
  return (
    <Wrapper>
      <Img src={backUrl} alt="counterBack" />
      <IconWrapper>
        {children}
        <Count>{count}</Count>
      </IconWrapper>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex-1 flex justify-center items-center flex-col relative select-none p-8 3xl:p-16 4xl:p-20`}
`;

const Img = styled.img`
  ${tw`absolute w-full h-full object-contain opacity-80 pointer-events-none`}
`;

const IconWrapper = styled.div`
  ${tw`z-10 flex justify-center items-center flex-col`}
`;

const Count = styled.span`
  ${tw`text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all font-bold`}
`;
export default Counter;
