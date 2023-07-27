import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { Tab, Tabs, TabList, TabPanel } from "react-tabs";
import "react-tabs/style/react-tabs.css";
import { trackWindowScroll } from "react-lazy-load-image-component";

import Iconmonstr from "./tabs/iconmonstr";
import Heroicons from "./tabs/heroicons";
import Freepik from "./tabs/freepik";
import Flaticon from "./tabs/flaticon";
import Osu from "./tabs/osu";
import Unsplash from "./tabs/unsplash";

import { PAGES_CREDIT } from "../../config/texts";

const Credit = ({ scrollPosition }) => {
  return (
    <Wrapper>
      <UpperBox>
        <Title>{PAGES_CREDIT.title}</Title>
      </UpperBox>
      <LowerBox>
        <TabsStyled>
          <TabList>
            <Tab>{PAGES_CREDIT.tabLists.iconmonstr}</Tab>
            <Tab>{PAGES_CREDIT.tabLists.heroicons}</Tab>
            <Tab>{PAGES_CREDIT.tabLists.freepik}</Tab>
            <Tab>{PAGES_CREDIT.tabLists.flaticon}</Tab>
            <Tab>{PAGES_CREDIT.tabLists.OSU}</Tab>
            <Tab>{PAGES_CREDIT.tabLists.unsplash}</Tab>
          </TabList>

          <TabPanel>
            <Container>
              <Iconmonstr />
            </Container>
          </TabPanel>

          <TabPanel>
            <Container>
              <Heroicons />
            </Container>
          </TabPanel>

          <TabPanel>
            <Container>
              <Freepik scrollPosition={scrollPosition} />
            </Container>
          </TabPanel>

          <TabPanel>
            <Container>
              <Flaticon scrollPosition={scrollPosition} />
            </Container>
          </TabPanel>

          <TabPanel>
            <Container>
              <Osu scrollPosition={scrollPosition} />
            </Container>
          </TabPanel>

          <TabPanel>
            <Container>
              <Unsplash scrollPosition={scrollPosition} />
            </Container>
          </TabPanel>
        </TabsStyled>
      </LowerBox>
    </Wrapper>
  );
};

const Wrapper = styled.section`
  ${tw`min-h-screen `}
`;

const UpperBox = styled.div`
  ${tw`flex items-center lg:justify-center my-2 mx-2 md:my-4 xl:my-6 `}
`;

const LowerBox = styled.div`
  ${tw`flex justify-center text-base lg:text-xl 2xl:text-2xl 3xl:text-3xl 4xl:text-4xl transition-all`}
`;

const Title = styled.h1`
  ${tw`lg:w-[85%] mb-2 md:mb-4 xl:mb-8 font-bold mt-10 underline transition-all text-xl sm:text-2xl md:text-3xl lg:text-4xl 2xl:text-5xl 3xl:text-7xl 4xl:text-9xl`}
`;

const Container = styled.div`
  ${tw`flex flex-wrap justify-center`}
`;

const TabsStyled = styled(Tabs)`
  ${tw`w-full lg:w-[85%] font-light`}
`;
export default trackWindowScroll(Credit);
