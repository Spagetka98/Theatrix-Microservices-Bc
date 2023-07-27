import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Header from "../components/header/index";
import Quote from "../components/quote";
import Footer from "../components/footer/index";

const Layout = ({ children }) => {
  return (
    <>
      <Header />
      <Quote />
      <Body>{children}</Body>
      <Footer />
    </>
  );
};

const Body = styled.main`
  ${tw`h-full`}
`;
export default Layout;
