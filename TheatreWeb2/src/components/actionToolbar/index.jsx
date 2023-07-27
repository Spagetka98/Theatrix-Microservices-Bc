import React, { useState, useEffect } from "react";
import styled from "styled-components";
import tw from "twin.macro";

import ThumbUp from "../../assets/thumbUp";
import ThumbDown from "../../assets/thumbDown";
import Chat from "../../assets/chat";

import {
  ADD_LIKED_ACTIVITY,
  ADD_DISLIKED_ACITIVITY,
} from "../../config/axios/endpoints";

import { COMP_ACTION_TOOL_BAR } from "../../config/texts";

import useAxios from "../../hooks/useAxios";

import Box from "./box";
import FloatingBox from "./floatingBox";

const ActionToolbar = ({
  id,
  likedByUser = false,
  dislikedByUser = false,
  ratedByUser = false,
  totalLiked = 0,
  totalDisliked = 0,
  totalRated = 0,
  hideRatingBox = false,
}) => {
  const [actionStatus, setActionStatus] = useState({
    likedByUser,
    totalLiked,
    dislikedByUser,
    totalDisliked,
    ratedByUser,
    totalRated,
  });
  const [msg, setMsg] = useState(null);
  const [data, error, loading, setConfigParams] = useAxios();

  useEffect(() => {
    if (data) {
      setActionStatus(data);
    }
  }, [data]);

  useEffect(() => {
    if (error) {
      setMsg(COMP_ACTION_TOOL_BAR.errorMsg);
    }
  }, [error]);

  const likedHandler = () => {
    if (loading) return;
    setMsg(null);
    setConfigParams(ADD_LIKED_ACTIVITY.url, ADD_LIKED_ACTIVITY.method, {
      idActivity: id,
    });
  };

  const dislikedHandler = () => {
    if (loading) return;
    setMsg(null);
    setConfigParams(ADD_DISLIKED_ACITIVITY.url, ADD_DISLIKED_ACITIVITY.method, {
      idActivity: id,
    });
  };

  return (
    <Wrapper>
      <Row>
        <Box
          onClick={likedHandler}
          count={actionStatus?.totalLiked}
          active={actionStatus?.likedByUser}
          color="#afe4967f"
        >
          <ThumbUp fillColor="#5bbb05" />
        </Box>
        {!hideRatingBox && (
          <Box
            count={actionStatus?.totalRated}
            active={actionStatus?.ratedByUser}
            color="#fdba027c"
          >
            <Chat fillColor="#fdba02" />
          </Box>
        )}
        <Box
          onClick={dislikedHandler}
          count={actionStatus?.totalDisliked}
          active={actionStatus?.dislikedByUser}
          color="#f8797984"
        >
          <ThumbDown fillColor="#c50404" />
        </Box>
      </Row>

      <FloatingBox loading={loading} msg={msg} />
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`w-full h-full relative cursor-pointer`}
`;

const Row = styled.div`
  ${tw`flex h-full`}
`;

export default ActionToolbar;
