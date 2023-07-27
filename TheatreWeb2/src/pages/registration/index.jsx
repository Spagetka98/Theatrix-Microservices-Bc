import React from "react";
import { motion } from "framer-motion";
import styled from "styled-components";
import tw from "twin.macro";
import { useMediaQuery } from "react-responsive";

import Registration from "../../assets/freepik/registration";

import { TABLET } from "../../config/sizes";
import { PAGES_REGISTRATION } from "../../config/texts";

import {
  usernameValidation,
  emailValidation,
} from "../../helpersFunc/registrationValidation";

import PasswordsCheck from "../../components/passwordsCheck";
import FormInput from "../../components/formInput";
import FormButton from "../../components/formButton";

import useRegistration from "./service";

import Snackbar from "@mui/material/Snackbar";

const RegistrationPage = () => {
  const isMobile = useMediaQuery({
    query: TABLET,
  });

  const {
    usernameBoxRef,
    emailBoxRef,
    passwordBoxRef,
    loading,
    message,
    registrationHandler,
  } = useRegistration();

  return (
    <Wrapper>
      {!isMobile && (
        <ImgContainer>
          <Image
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ type: "spring", duration: 2 }}
            ismobile={isMobile ? 1 : 0}
            src={Registration[0].imgUrl}
            alt="registration"
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
            {PAGES_REGISTRATION.title}
          </Title>
        </TitleContainer>
        {isMobile && (
          <ImgContainer>
            <Image
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ type: "spring", duration: 2 }}
              ismobile={isMobile ? 1 : 0}
              src={Registration[0].imgUrl}
              alt="registration"
            />
          </ImgContainer>
        )}
        <FormContainer>
          <FormInput
            ref={usernameBoxRef}
            title={PAGES_REGISTRATION.username}
            placeHolder={PAGES_REGISTRATION.usernamePlaceholder}
            loading={loading}
            delay={1.4}
            validationFunction={usernameValidation}
          />

          <FormInput
            ref={emailBoxRef}
            title={PAGES_REGISTRATION.email}
            placeHolder={PAGES_REGISTRATION.emailPlaceholder}
            loading={loading}
            delay={1.8}
            validationFunction={emailValidation}
          />

          <PasswordsCheck
            ref={passwordBoxRef}
            loading={loading}
            delayFirstBox={2.2}
            delaySecondBox={2.4}
          />

          <FormButton
            loading={loading}
            message={message}
            btnText={PAGES_REGISTRATION.regBtn}
            clickHandler={registrationHandler}
            delay={2.6}
          />
        </FormContainer>
      </TextContainer>

      <Snackbar open={loading} autoHideDuration={9999999}>
        <SnackBody>{PAGES_REGISTRATION.snackBar}</SnackBody>
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
  ${tw`flex-1 flex flex-col mb-8 mt-14 lg:mt-24`}
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
export default RegistrationPage;
