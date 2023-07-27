import React from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Slider from "../slider";
import ClockSpinner from "../clockSpinner";
import ErrorWarning from "../errorWarning";

import useAxios from "../../hooks/useAxios";

import { COMP_BEST_ACTIVITIES } from "../../config/texts";

import { GET_TOP_ACTIVITIES } from "../../config/axios/endpoints";

const BestActivities = () => {
  const [data, error, loading] = useAxios(
    GET_TOP_ACTIVITIES.method,
    GET_TOP_ACTIVITIES.url
  );
  return (
    <Wrapper>
      <Title>{COMP_BEST_ACTIVITIES.title}</Title>
      {loading ? (
        <ClockSpinner loading={loading} text={COMP_BEST_ACTIVITIES.spinner} />
      ) : error ? (
        <ErrorWarning style={{ "--minHeight": "75vh" }} />
      ) : (
        data && <Slider activities={data} />
      )}
    </Wrapper>
  );
};

const Wrapper = styled.section`
  ${tw`min-h-[50vh] flex lg:p-4 3xl:p-8 flex-col`}
`;

const Title = styled.h1.attrs({
  className: "decoration-purple-500 decoration-2 ",
})`
  ${tw`w-full p-2 md:p-6 3xl:p-10 text-center font-bold underline transition-all text-xl sm:text-2xl md:text-3xl lg:text-4xl 2xl:text-5xl 3xl:text-6xl 4xl:text-7xl`}
`;

export default BestActivities;
