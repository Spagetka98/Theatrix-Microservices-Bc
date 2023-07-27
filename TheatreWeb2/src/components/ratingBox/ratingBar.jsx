import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Star from "../../assets/star";

const RatingBar = ({ numberOfStar, filling, totalCount, fillingColor }) => {
  return (
    <Wrapper>
      <StarBox>
        <StarNumber>{numberOfStar + "x"}</StarNumber>
        <Star
          fillColor={"#f1c92b"}
          style={{ "--width": "40%", "--height": "100%" }}
        />
      </StarBox>
      <LinesBox>
        <Line
          style={{
            "--width": filling === 0 ? 5 + "%" : filling + "%",
            "--bgColor": fillingColor,
          }}
        />
      </LinesBox>
      <CountBox>
        <Count>{totalCount + "x"}</Count>
      </CountBox>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex justify-center my-2 mx-1`}
`;

const StarBox = styled.div`
  ${tw`flex-1 flex justify-center items-center`}
`;

const LinesBox = styled.div.attrs({ className: "flex-[4_4_0%]" })`
  ${tw``}
`;

const Line = styled.div`
  ${tw`h-full w-[var(--width,"0%")] bg-[var(--bgColor,"#c9c9c9")] border-2 border-black`}
`;

const CountBox = styled.div`
  ${tw`flex-1`}
`;

const Count = styled.div`
  ${tw`h-full flex justify-center items-center`}
`;

const StarNumber = styled.span`
  ${tw`text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all font-semibold pr-2`}
`;
export default RatingBar;
