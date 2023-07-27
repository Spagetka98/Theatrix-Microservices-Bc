import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import { PAGES_CREDIT_ICON_BOX } from "../../config/texts";

const IconBox = ({ children, href }) => {
  return (
    <Container>
      <IconSection>{children}</IconSection>
      <TextSection>
        <SubTitle>{PAGES_CREDIT_ICON_BOX.title}</SubTitle>
        <Link href={href} target="_blank" rel="noopener noreferrer">
          {PAGES_CREDIT_ICON_BOX.link}
        </Link>
      </TextSection>
    </Container>
  );
};

const Container = styled.div`
  ${tw`flex flex-col mx-2`}
`;

const IconSection = styled.div`
  ${tw`flex-1 flex justify-center items-center`}
`;

const TextSection = styled.div.attrs({ className: "flex-[7_7_0%]" })`
  ${tw`flex flex-col items-center`}
`;

const SubTitle = styled.h2`
  ${tw`font-semibold pr-2`}
`;

const Link = styled.a`
  ${tw`text-blue-500 md:hover:opacity-50 transition-all`}
`;
export default IconBox;
