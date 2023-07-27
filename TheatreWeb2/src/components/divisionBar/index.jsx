import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import styled from "styled-components";
import tw from "twin.macro";
import { useMediaQuery } from "react-responsive";
import { motion } from "framer-motion";
import isEqual from "lodash/isEqual";

import DivisionBox from "./divisionBox";
import ClockSpinner from "../clockSpinner";
import ErrorWarning from "../errorWarning";

import useAxios from "../../hooks/useAxios";

import colors from "../../config/colors/division";
import { FULL_HD } from "../../config/sizes";

import { GET_ALL_DIVISIONS } from "../../config/axios/endpoints";
import { COMP_DIVISIONBAR_OPTIONS } from "../../config/texts";

const DivisionBar = ({ addDivisionCallback, pageCallback, state }) => {
  const dispatch = useDispatch();
  const [data, error, loading] = useAxios(
    GET_ALL_DIVISIONS.method,
    GET_ALL_DIVISIONS.url
  );
  const [collapse, setCollapse] = useState(false);
  const isQ = useMediaQuery({
    query: FULL_HD,
  });

  useEffect(() => {
    if (data && !state) {
      dispatch(addDivisionCallback(data));
    }
  }, [data, addDivisionCallback, state, dispatch]);

  const addDivision = (divisionName) => {
    const divisionAdded = [...state, divisionName];

    dispatch(addDivisionCallback(divisionAdded));
    dispatch(pageCallback(1));
  };

  const removeDivision = (divisionName) => {
    const divisionsLeft = state?.filter(
      (division, index) => division !== divisionName
    );

    dispatch(addDivisionCallback(divisionsLeft));
    dispatch(pageCallback(1));
  };

  const removeAllDivision = () => {
    if (state instanceof Array && state.length === 0) return;

    dispatch(addDivisionCallback([]));
    dispatch(pageCallback(1));
  };

  const addAllDivision = () => {
    if (state instanceof Array && isEqual(state, data)) return;

    dispatch(addDivisionCallback(data));
    dispatch(pageCallback(1));
  };

  return (
    <Wrapper>
      {loading ? (
        <ClockSpinner
          loading={loading}
          size={isQ ? 50 : 100}
          style={{ "--minHeight": "0" }}
        />
      ) : error ? (
        <ErrorWarning hideImg hideTitle style={{ "--minHeight": "0" }} />
      ) : (
        <DivisionWrapper>
          <Button active={collapse} onClick={() => setCollapse(!collapse)}>
            {COMP_DIVISIONBAR_OPTIONS.categories}
          </Button>

          <CollapseBox collapse={collapse}>
            {data?.map((name, index) => {
              return (
                <DivisionBox
                  addDivisionHandler={addDivision}
                  removeDivisionHandler={removeDivision}
                  key={index}
                  name={name}
                  color={colors[index % colors.length]}
                  active={state?.includes(name)}
                />
              );
            })}

            <CategoryOptionsWrapper>
              <CategoryRemoveButton
                whileHover={{ scale: 0.97 }}
                onClick={removeAllDivision}
              >
                {COMP_DIVISIONBAR_OPTIONS.removeAllCategories}
              </CategoryRemoveButton>
              <CategoryAddAllButton
                whileHover={{ scale: 0.97 }}
                onClick={addAllDivision}
              >
                {COMP_DIVISIONBAR_OPTIONS.addAllCategories}
              </CategoryAddAllButton>
            </CategoryOptionsWrapper>
          </CollapseBox>
        </DivisionWrapper>
      )}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex flex-wrap py-4 w-[75%] flex-col`}
`;

const DivisionWrapper = styled.div`
  ${tw`flex flex-col`}
`;

const Button = styled.button(({ active }) => [
  tw`p-2 my-4 border-2 xl:border-4 4xl:border-8 border-gray-300 hover:border-purple-500 shadow-lg bg-[#f4f4f4] rounded-lg font-semibold min-w-[50px]`,
  active && tw`border-purple-500`,
]);

const CollapseBox = styled.div(({ collapse }) => [
  tw`flex flex-wrap items-center justify-center`,
  !collapse && tw`hidden`,
]);

const CategoryOptionsWrapper = styled.div`
  ${tw`flex flex-wrap items-center justify-center sm:justify-between w-full`}
`;

const CategoryOptionsButton = styled(motion.button)`
  ${tw`p-1 border-b-2 italic min-w-min my-2 whitespace-nowrap text-center mx-3 font-extralight cursor-pointer select-none text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all`}
`;

const CategoryRemoveButton = styled(CategoryOptionsButton)`
  ${tw`border-red-300`}
`;

const CategoryAddAllButton = styled(CategoryOptionsButton)`
  ${tw`border-green-300`}
`;

export default DivisionBar;
