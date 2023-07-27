import React, {
  useState,
  forwardRef,
  useImperativeHandle,
  useCallback,
  useRef,
} from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { motion } from "framer-motion";

import { passValidation } from "../../helpersFunc/registrationValidation";

import { COMP_PASSWORDS_CHECK } from "../../config/texts";

const PasswordsCheck = forwardRef(
  ({ loading, delayFirstBox = 0, delaySecondBox = 0 }, ref) => {
    const newPasswordRef = useRef(null);
    const correctNewPasswordRef = useRef(null);

    const [newPassword, setNewPassword] = useState(null);
    const [correctNewPassword, setCorrectNewPassword] = useState(null);
    const [passwordError, setPasswordError] = useState(null);

    const newPasswordHandler = (e) => {
      setNewPassword(e.target.value);

      errorCheck(e.target.value, correctNewPassword);
    };

    const correctNewPasswordHandler = (e) => {
      setCorrectNewPassword(e.target.value);

      errorCheck(newPassword, e.target.value);
    };

    const errorCheck = useCallback((pass1, pass2) => {
      const errorMsg = passValidation(pass1, pass2);
      if (errorMsg) {
        setPasswordError(errorMsg);
      } else {
        setPasswordError(null);
      }
    }, []);

    useImperativeHandle(
      ref,
      () => ({
        getInputData: () => {
          errorCheck(newPassword, newPassword);
          return passwordError ? null : newPassword;
        },
        clearData: () => {
          newPasswordRef.current.value = "";
          correctNewPasswordRef.current.value = "";
          setNewPassword(null);
          setCorrectNewPassword(null);
          setPasswordError(null);
        },
      }),
      [passwordError, newPassword, errorCheck]
    );

    return (
      <>
        <Label
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ type: "spring", duration: 1, delay: delayFirstBox }}
        >
          <Title>{COMP_PASSWORDS_CHECK.newPassword}</Title>
          <Input
            ref={newPasswordRef}
            onChange={newPasswordHandler}
            placeholder={COMP_PASSWORDS_CHECK.newPasswordPlaceholder}
            type="password"
            disabled={loading}
          />
        </Label>
        <Label
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ type: "spring", duration: 1, delay: delaySecondBox }}
        >
          <Title>{COMP_PASSWORDS_CHECK.correctPassword}</Title>
          <Input
            ref={correctNewPasswordRef}
            onChange={correctNewPasswordHandler}
            placeholder={COMP_PASSWORDS_CHECK.correctPasswordPlaceholder}
            type="password"
            disabled={loading}
          />
        </Label>

        <Error
          initial={{ opacity: 0 }}
          animate={{
            opacity: passwordError ? (loading ? 0 : 1) : 0,
          }}
          transition={{
            type: "spring",
            duration: passwordError ? (loading ? 0 : 3) : 0,
          }}
        >
          {passwordError}
        </Error>
      </>
    );
  }
);

const Label = styled(motion.label)`
  ${tw`flex flex-col w-[85%] md:w-[65%] mt-10 3xl:mt-16 4xl:mt-32 font-medium`}
`;

const Title = styled.span`
  ${tw`pb-2 pl-1 3xl:pb-4 3xl:pl-2`}
`;

const Input = styled.input.attrs({
  className: "border-slate-300 md:text-md",
})`
  ${tw`p-1 focus:shadow-xl focus:border-purple-400 focus:border-b-4 border-b-2 3xl:p-2 4xl:focus:border-b-8 4xl:border-b-4 outline-none font-extralight text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-5xl transition-all`}
`;

const Error = styled(motion.span).attrs({ className: "md:text-md" })`
  ${tw`text-red-500 w-[85%] md:w-[65%] min-h-[65px] lg:min-h-[85px] xl:min-h-[100px] 3xl:min-h-[200px] italic text-center opacity-0 lg:text-lg xl:text-xl 2xl:text-2xl 3xl:text-3xl 4xl:text-5xl transition-all`},
`;

export default PasswordsCheck;
