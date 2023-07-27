import React, {
  useEffect,
  forwardRef,
  useImperativeHandle,
  useCallback,
} from "react";

import useAxios from "../../hooks/useAxios";

import { GET_ACTIVITY_RATING_NUMBERS } from "../../config/axios/endpoints";

import RatingContainer from "./ratingContainer";

import ClockSpinner from "../../components/clockSpinner";
import ErrorWarning from "../../components/errorWarning";

const RatingBox = forwardRef(({ id }, ref) => {
  const [data, error, loading, setConfigParams] = useAxios();

  const setAxios = useCallback(() => {
    if (id) {
      setConfigParams(
        GET_ACTIVITY_RATING_NUMBERS.url,
        GET_ACTIVITY_RATING_NUMBERS.method,
        { id: id }
      );
    }
  }, [id, setConfigParams]);

  useImperativeHandle(
    ref,
    () => ({
      setAxios,
    }),
    [setAxios]
  );

  useEffect(() => {
    setAxios();
  }, [setAxios]);

  return loading ? (
    <ClockSpinner style={{ "--minHeight": "0" }} />
  ) : error ? (
    <ErrorWarning hideImg />
  ) : (
    <RatingContainer
      totalRatings={data?.totalRated}
      totalRating={data?.activityRating}
      ratings={[
        data?.totalFiveRatings,
        data?.totalFourRatings,
        data?.totalThreeRatings,
        data?.totalTwoRatings,
        data?.totalOneRatings,
      ]}
    />
  );
});

export default RatingBox;
