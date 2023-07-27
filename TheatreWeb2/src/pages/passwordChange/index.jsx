import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { useMediaQuery } from "react-responsive";
import { motion } from "framer-motion";

import { TABLET } from "../../config/sizes";
import { PAGES_PASSWORD_CHANGE } from "../../config/texts";

import PassChange from "../../assets/freepik/change";

import FormInput from "../../components/formInput";
import FormButton from "../../components/formButton";
import withAuth from "../../components/withAuth";
import PasswordsCheck from "../../components/passwordsCheck";

import usePasswordChange from "./service";

const PasswordChange = () => {
  const {
    formInputRef,
    passwordsBoxesRef,
    message,
    changeHandler,
    emptyValidation,
    loading,
  } = usePasswordChange();
  const isMobile = useMediaQuery({
    query: TABLET,
  });

  return (
    <Wrapper>
      {!isMobile && (
        <ImgBox>
          <Img
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ type: "spring", duration: 1 }}
            src={PassChange[0].imgUrl}
            alt="password"
            ismobile={isMobile ? 1 : 0}
          />
        </ImgBox>
      )}
      <FormBox>
        <Form>
          <Title
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ type: "spring", duration: 1, delay: 0.5 }}
          >
            {PAGES_PASSWORD_CHANGE.title}
          </Title>
          {isMobile && (
            <Img
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ type: "spring", duration: 1 }}
              src={PassChange[0].imgUrl}
              alt="password"
              ismobile={isMobile ? 1 : 0}
            />
          )}
          <FormInput
            ref={formInputRef}
            title={PAGES_PASSWORD_CHANGE.formInput.title}
            placeHolder={PAGES_PASSWORD_CHANGE.formInput.placeholder}
            loading={loading}
            validationFunction={emptyValidation}
            type="password"
            delay={1}
          />

          <PasswordsCheck
            loading={loading}
            ref={passwordsBoxesRef}
            delayFirstBox={1.5}
            delaySecondBox={2}
          />

          <FormButton
            loading={loading}
            message={message}
            btnText={PAGES_PASSWORD_CHANGE.btnChange}
            clickHandler={changeHandler}
            delay={2.5}
          />
        </Form>
      </FormBox>
    </Wrapper>
  );
};

const Wrapper = styled.section`
  ${tw`min-h-screen flex`}
`;

const ImgBox = styled.div`
  ${tw`flex-1 flex justify-center items-center`}
`;

const FormBox = styled.div`
  ${tw`flex-1 flex flex-col mb-8 mt-14 lg:mt-20`}
`;

const Img = styled(motion.img)(({ ismobile }) => [
  tw`object-contain`,
  ismobile && tw`max-h-[300px]`,
]);

const Title = styled(motion.h1).attrs({
  className: "bg-gradient-to-r from-pink-500 to-violet-500",
})`
  ${tw`transition-all py-4 3xl:py-6 text-3xl lg:text-4xl 2xl:text-5xl 3xl:text-7xl 4xl:text-9xl font-extrabold bg-clip-text text-transparent`}
`;

const Form = styled.form.attrs({ className: "flex-[2_2_0%]" })`
  ${tw`flex justify-center items-center flex-col font-light text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all`}
`;

export default withAuth(PasswordChange);
