import React, {
  useState,
  useImperativeHandle,
  forwardRef,
  useCallback,
  useRef,
} from "react";
import { motion } from "framer-motion";
import styled from "styled-components";
import tw from "twin.macro";

const FormInput = forwardRef(
  (
    { title, placeHolder, loading, delay, validationFunction, type = "text" },
    ref
  ) => {
    const inputRef = useRef(null);
    const [inputData, setInputData] = useState(null);
    const [inputError, setInputError] = useState(null);

    const inputHandler = (e) => {
      setInputData(e.target.value);

      validationFunction && errorCheck(e.target.value);
    };

    const errorCheck = useCallback(
      (data) => {
        if (!validationFunction) return;
        const erroMsg = validationFunction(data);

        if (erroMsg) {
          setInputError(erroMsg);
        } else {
          setInputError(null);
        }
      },
      [validationFunction]
    );

    useImperativeHandle(
      ref,
      () => ({
        getInputData: () => {
          validationFunction && errorCheck(inputData);
          return inputError ? null : inputData;
        },
        clearData: () => {
          setInputData(null);
          setInputError(null);
          inputRef.current.value = "";
        },
      }),
      [inputError, inputData, errorCheck, validationFunction]
    );
    return (
      <>
        <Label
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ type: "spring", duration: 1, delay: delay }}
        >
          <Title>{title}</Title>
          <Input
            ref={inputRef}
            onChange={inputHandler}
            type={type}
            placeholder={placeHolder}
            disabled={loading}
          />
        </Label>

        <Error
          initial={{ opacity: 0 }}
          animate={{
            opacity: inputError ? (loading ? 0 : 1) : 0,
          }}
          transition={{
            type: "spring",
            duration: inputError ? (loading ? 0 : 3) : 0,
          }}
        >
          {inputError}
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
  ${tw`text-red-500 w-[85%] md:w-[65%] min-h-[50px] lg:min-h-[60px] xl:min-h-[70px] 3xl:min-h-[80px] italic text-center opacity-0 text-base lg:text-lg xl:text-xl 2xl:text-2xl 3xl:text-3xl 4xl:text-5xl transition-all`},
`;

export default FormInput;
