import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import { PAGES_CREDIT } from "../../../../config/texts";

const heroicons = () => {
  return (
    <Column>
      <SubTitle>{PAGES_CREDIT.iconUsed}</SubTitle>
      <Link href={"https://heroicons.com/"}>{PAGES_CREDIT.iconLink}</Link>
    </Column>
  );
};

const SubTitle = styled.h2`
  ${tw`font-semibold pr-2`}
`;

const Link = styled.a`
  ${tw`text-blue-500 md:hover:opacity-50 transition-all`}
`;

const Column = styled.div`
  ${tw`flex flex-col justify-center items-center`}
`;

export default heroicons;
