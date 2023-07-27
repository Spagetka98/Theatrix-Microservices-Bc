import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import { COMP_ACTIVITIES_LAST_BOX } from "../../config/texts";

const LastBox = () => {
  return (
    <Box>
      <Title>{COMP_ACTIVITIES_LAST_BOX.title}</Title>
      <LastContainer>
        <TextLine>{COMP_ACTIVITIES_LAST_BOX.upperText}</TextLine>
        <List>
          <li>
            <Bold>{COMP_ACTIVITIES_LAST_BOX.list.week}</Bold>
            {COMP_ACTIVITIES_LAST_BOX.list.weekOpeningTime}
          </li>
          <li>
            <Bold>{COMP_ACTIVITIES_LAST_BOX.list.saturday}</Bold>
            {COMP_ACTIVITIES_LAST_BOX.list.saturdayOpeningTime}
          </li>
          <li>
            <Bold>{COMP_ACTIVITIES_LAST_BOX.list.sunday}</Bold>
            {COMP_ACTIVITIES_LAST_BOX.list.sundayOpeningTime}
          </li>
        </List>
        <TextLine>{COMP_ACTIVITIES_LAST_BOX.tel}</TextLine>
        <TextLine>{COMP_ACTIVITIES_LAST_BOX.lowerText}</TextLine>
      </LastContainer>
    </Box>
  );
};

const Box = styled.div`
  ${tw`border-4 flex flex-col col-span-1 rounded-md bg-gray-100 font-extralight text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all`}
`;

const Title = styled.span.attrs({
  className:
    "bg-clip-text text-transparent bg-gradient-to-r from-pink-500 to-violet-500 flex-[2_2_0%]",
})`
  ${tw`text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all font-extrabold uppercase px-2 flex items-center`}
`;

const LastContainer = styled.div.attrs({ className: "flex-[8_8_0%]" })`
  ${tw`flex flex-col px-2`}
`;

const TextLine = styled.p`
  ${tw`py-2`}
`;

const List = styled.ul`
  ${tw`list-disc ml-8`}
`;

const Bold = styled.span`
  ${tw`font-bold`}
`;

export default LastBox;
