import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { motion } from "framer-motion";
import { useMediaQuery } from "react-responsive";

import { TABLET, XL } from "../../config/sizes";

import Spinner from "../../components/clockSpinner";

const FormButton = ({ loading, message, btnText, clickHandler, delay = 0 }) => {
  const isMobile = useMediaQuery({
    query: TABLET,
  });
  const isXl = useMediaQuery({
    query: XL,
  });

  return (
    <Container
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ type: "spring", duration: 1, delay: delay }}
    >
      {loading ? (
        <Spinner
          style={{ "--minHeight": "0" }}
          size={isMobile ? 75 : isXl ? 115 : 250}
        />
      ) : (
        <Button type="submit" value={btnText} onClick={clickHandler} />
      )}
      <Message
        iserror={message?.isError ? 1 : 0}
        initial={{ opacity: 0 }}
        animate={{
          opacity: message ? (loading ? 0 : 1) : 0,
        }}
        transition={{
          type: "spring",
          duration: message ? (loading ? 0 : 3) : 0,
        }}
      >
        {message?.msg}
      </Message>
    </Container>
  );
};

const Button = styled.input.attrs({ className: "bg-slate-100" })`
  ${tw`p-4 mt-10 hover:border-gray-400 transition-all 3xl:p-8 4xl:p-12 rounded-full shadow-xl border-2 border-gray-300 cursor-pointer mb-10`}
`;

const Container = styled(motion.div)`
  ${tw`min-h-[200px] w-[60%] flex flex-col`}
`;

const Message = styled(motion.span).attrs({ className: "md:text-md" })(
  ({ iserror }) => [
    tw`italic text-center opacity-0 text-base lg:text-lg xl:text-xl 2xl:text-2xl 3xl:text-3xl 4xl:text-5xl transition-all`,
    !iserror ? tw`text-green-500` : tw`text-red-500`,
  ]
);
export default FormButton;
