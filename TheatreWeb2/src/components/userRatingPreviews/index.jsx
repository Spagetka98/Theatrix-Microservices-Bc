import React, { useEffect } from "react";

import RatingPreviews from "../ratingPreviews";

import useAxios from "../../hooks/useAxios";

import { GET_ACTIVITIES_DETAILS } from "../../config/axios/endpoints";

const UserRatingPreviews = ({ previews }) => {
  const [data, error, loading, setConfigParams] = useAxios();

  useEffect(() => {
    const Ids = previews.map((review) => review.activityID);

    const configData = {
      ids: Ids,
    };

    setConfigParams(
      GET_ACTIVITIES_DETAILS.url,
      GET_ACTIVITIES_DETAILS.method,
      configData
    );
  }, [previews, setConfigParams]);

  return (
    <RatingPreviews data={previews} previewDetails={{ data, error, loading }} />
  );
};

export default UserRatingPreviews;
