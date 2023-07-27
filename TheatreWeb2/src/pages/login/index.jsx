import React from "react";
import { motion } from "framer-motion";
import styled from "styled-components";
import tw from "twin.macro";
import { useMediaQuery } from "react-responsive";

import SignIn from "../../assets/freepik/signIn";

import { TABLET } from "../../config/sizes";
import { PAGES_LOGIN } from "../../config/texts";

import FormInput from "../../components/formInput";
import FormButton from "../../components/formButton";

import useLogin from "./service";

import Snackbar from "@mui/material/Snackbar";

const LoginPage = () => {
  const isMobile = useMediaQuery({
    query: TABLET,
  });
  const {
    usernameRef,
    passwordRef,
    loginError,
    loading,
    loginHandler,
    emptyValidation,
  } = useLogin();

  return (
    <Wrapper>
      {!isMobile && (
        <ImgContainer>
          <Image
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ type: "spring", duration: 2 }}
            ismobile={isMobile ? 1 : 0}
            src={SignIn[0].imgUrl}
            alt="profile"
          />
        </ImgContainer>
      )}
      <TextContainer>
        <TitleContainer>
          <Title
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ type: "spring", duration: 1, delay: 0.5 }}
          >
            {PAGES_LOGIN.title}
          </Title>
        </TitleContainer>
        {isMobile && (
          <ImgContainer>
            <Image
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ type: "spring", duration: 2 }}
              ismobile={isMobile ? 1 : 0}
              src={SignIn[0].imgUrl}
              alt="profile"
            />
          </ImgContainer>
        )}
        <FormContainer>
          <FormInput
            ref={usernameRef}
            title={PAGES_LOGIN.name}
            placeHolder={PAGES_LOGIN.namePlaceHolder}
            loading={loading}
            validationFunction={emptyValidation}
            delay={1.4}
          />

          <FormInput
            ref={passwordRef}
            title={PAGES_LOGIN.password}
            placeHolder={PAGES_LOGIN.passwordPlaceHolder}
            loading={loading}
            validationFunction={emptyValidation}
            delay={1.8}
            type="password"
          />

          <FormButton
            loading={loading}
            message={loginError}
            btnText={PAGES_LOGIN.logBtn}
            clickHandler={loginHandler}
            delay={2.2}
          />
        </FormContainer>
      </TextContainer>

      <Snackbar open={loading} autoHideDuration={9999999}>
        <SnackBody>{PAGES_LOGIN.snackBar}</SnackBody>
      </Snackbar>
    </Wrapper>
  );
};

const Wrapper = styled.section`
  ${tw`min-h-screen flex pt-6`}
`;

const ImgContainer = styled.div`
  ${tw`flex-1 flex justify-center items-center`}
`;

const Image = styled(motion.img)(({ ismobile }) => [
  tw`object-contain`,
  ismobile && tw`max-h-[300px]`,
]);

const TextContainer = styled.div`
  ${tw`flex-1 flex flex-col mb-8 mt-14 lg:mt-20 xl:mt-0`}
`;

const TitleContainer = styled.div`
  ${tw`flex-1 flex items-end justify-center`}
`;

const Title = styled(motion.h1).attrs({
  className: "bg-gradient-to-r from-pink-500 to-violet-500",
})`
  ${tw`transition-all py-4 3xl:py-6 text-3xl lg:text-4xl 2xl:text-5xl 3xl:text-7xl 4xl:text-9xl font-extrabold bg-clip-text text-transparent`}
`;

const FormContainer = styled.form.attrs({ className: "flex-[2_2_0%]" })`
  ${tw`flex justify-center items-center flex-col font-light text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all`}
`;

const SnackBody = styled.div.attrs({ className: "bg-orange-500" })`
  ${tw`animate-bounce p-4 rounded-xl text-white shadow-xl font-extrabold text-base lg:text-lg xl:text-xl 2xl:text-2xl 3xl:text-3xl 4xl:text-5xl transition-all text-center`}
`;
export default LoginPage;
