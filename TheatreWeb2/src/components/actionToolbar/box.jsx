import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

const Box = ({ count, active = false, children, color, onClick }) => {
  return (
    <Wrapper onClick={onClick} status={active} style={{ "--bgColor": color }}>
      {children}
      <Count>{count}</Count>
    </Wrapper>
  );
};

const Wrapper = styled.div(({ status }) => [
  tw`justify-center items-center flex flex-1 flex-col transition-all duration-200 `,
  status
    ? tw`bg-[var(--bgColor,"#ffffff0")] hover:bg-transparent`
    : tw`bg-transparent hover:bg-[var(--bgColor,"#ffffff0")]`,
]);

const Count = styled.div`
  ${tw`font-extrabold pt-2 text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all`}
`;
export default Box;
