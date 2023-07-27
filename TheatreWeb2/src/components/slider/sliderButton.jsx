import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { motion } from "framer-motion";

const SliderButton = ({ onClick, children }) => {
  return (
    <Wrapper
      onClick={onClick}
      whileHover={{ scale: 1.1 }}
      whileTap={{ scale: 0.9 }}
    >
      {children}
    </Wrapper>
  );
};

const Wrapper = styled(motion.div)`
  ${tw`cursor-pointer shadow-xl border-2 border-gray-400 bg-gray-200 rounded-full w-[60%] h-[13%] max-w-[10rem] max-h-[10rem] flex justify-center items-center`}
`;

export default SliderButton;
