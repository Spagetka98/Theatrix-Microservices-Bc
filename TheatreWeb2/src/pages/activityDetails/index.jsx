import React, { useEffect } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { useParams } from "react-router-dom";

import useAxios from "../../hooks/useAxios";

import withAuth from "../../components/withAuth";
import ClockSpinner from "../../components/clockSpinner";
import ErrorWarning from "../../components/errorWarning";
import ActivityDetailsCard from "../../components/activityDetailsCard";

import { GET_ACTIVITY } from "../../config/axios/endpoints";

const ActivityDetails = () => {
  const { id } = useParams();
  const [data, error, loading, setConfigParams] = useAxios();

  useEffect(() => {
    if (id) {
      setConfigParams(GET_ACTIVITY.url, GET_ACTIVITY.method, { id: id });
    }
  }, [id, setConfigParams]);

  return (
    <Wrapper>
      {loading ? (
        <ClockSpinner style={{ "--minHeight": "100vh" }} />
      ) : error ? (
        <ErrorWarning hideImg />
      ) : (
        data && <ActivityDetailsCard id={id} data={data} />
      )}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`min-h-screen flex`}
`;

export default withAuth(ActivityDetails);
