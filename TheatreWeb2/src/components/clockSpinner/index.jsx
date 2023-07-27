import React, { useEffect, useState } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import ClipLoader from "react-spinners/ClipLoader";

import COLORS from "../../config/colors/spinner";

const ClockSpinner = ({ loading, size = 100, text, style }) => {
  const [color, setColor] = useState(null);

  useEffect(() => {
    let i = 0;

    const interval = setInterval(() => {
      i === COLORS.length ? (i = 0) : i++;
      setColor(COLORS[i]);
    }, 1200);

    return () => {
      clearInterval(interval);
    };
  }, []);

  return (
    <Wrapper style={style}>
      <ClipLoader loading={loading} size={size} color={color} />
      {text && <Title>{text}</Title>}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex-1 flex justify-center items-center flex-col min-h-[var(--minHeight,75vh)]`}
`;

const Title = styled.h2`
  ${tw`font-bold m-4 italic transition-all text-base sm:text-lg md:text-xl lg:text-2xl 3xl:text-3xl 4xl:text-4xl`}
`;
export default ClockSpinner;
