import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import OSU from "../../assets/osuLogo";

import Credit from "./columns/credit";
import Contacts from "./columns/contacts";
import Socials from "./columns/socials";

const Footer = () => {
  return (
    <footer>
      <Divider />
      <Body>
        <Wrapper>
          <ImgContainer>
            <Img src={OSU.imgUrl} alt="osuLogo" />
          </ImgContainer>

          <Credit />

          <Contacts />

          <Socials />
        </Wrapper>
      </Body>
    </footer>
  );
};

const Divider = styled.div.attrs({ className: "bg-slate-500" })`
  ${tw`min-h-[4vw] z-20 max-h-14 shadow-md `}
`;

const Wrapper = styled.div.attrs({ className: "bg-slate-200" })`
  ${tw` flex flex-col lg:flex-row font-light flex-1 max-w-[90%]`}
`;

const ImgContainer = styled.div`
  ${tw`flex-1 flex flex-col items-center justify-center mt-4`}
`;

const Img = styled.img`
  ${tw`object-contain w-[15%] lg:w-[50%] 3xl:w-[30%] min-w-[125px] transition-all pointer-events-none`}
`;

const Body = styled.div.attrs({ className: "bg-slate-200" })`
  ${tw`flex justify-center w-full `}
`;
export default Footer;
