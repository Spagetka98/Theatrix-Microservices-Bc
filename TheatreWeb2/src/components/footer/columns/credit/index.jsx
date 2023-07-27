import React from "react";
import { motion } from "framer-motion";
import styled from "styled-components";
import tw from "twin.macro";
import { Link } from "react-router-dom";

import { Name, Data } from "./config";

const Credit = () => {
  return (
    <Column>
      <Container>
        <Title>{Name}</Title>
        <Tab>
          {Data?.map((data, index) => {
            return (
              <Reference
                key={index}
                whileHover={{
                  scale: 0.95,
                  transition: { type: "spring", duration: 0.6 },
                }}
              >
                <Link to={data.link}>{data.title}</Link>
              </Reference>
            );
          })}
        </Tab>
      </Container>
    </Column>
  );
};
const Column = styled.div`
  ${tw`flex-1 flex flex-col mx-[10%] lg:mx-0`}
`;

const Container = styled.div`
  ${tw`min-w-[65%]`}
`;

const Title = styled.h3.attrs({ className: "w-fit border-orange-500" })`
  ${tw`pr-8 lg:pt-3 ml-1 mt-3  font-semibold text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-3xl 4xl:text-4xl transition-all border-b-2`}
`;

const Reference = styled(motion.div)`
  ${tw`ml-1 cursor-pointer lg:hover:opacity-60`}
`;

const Tab = styled.div`
  ${tw`flex pt-2 text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-2xl 4xl:text-3xl transition-all`}
`;

export default Credit;
