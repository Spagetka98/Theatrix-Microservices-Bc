import { useEffect, useState, useCallback } from "react";
import { useMediaQuery } from "react-responsive";
import { useNavigate } from "react-router-dom";
import moment from "moment";

import Actions from "../../components/ratingBar/actions";

import { PAGES_ACTIVITY_RATINGS_SERVICE } from "../../config/texts";
import { GET_RATING_PREVIEWS } from "../../config/axios/endpoints";
import { TABLET, FULL_HD } from "../../config/sizes";
import { DETAILS } from "../../config/navigation/paths";

import useAxios from "../../hooks/useAxios";

const useRatings = (id, ratingState, dispatch) => {
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState(null);
  const [data, error, loading, setConfigParams] = useAxios();

  const isTablet = useMediaQuery({
    query: TABLET,
  });
  const isPC = useMediaQuery({
    query: FULL_HD,
  });

  const pageHandler = (event, value) => {
    dispatch({ type: Actions.SET_PAGE, payload: value });
  };

  const dateCheck = useCallback((startDate, endDate) => {
    const dateStart = moment(startDate, "DD-MM-YYYY").toDate();
    const dateEnd = moment(endDate, "DD-MM-YYYY").toDate();

    if (
      dateStart <= dateEnd ||
      (dateStart && dateEnd == null) ||
      (dateStart == null && dateEnd)
    ) {
      setErrorMessage(null);
      return false;
    }
    setErrorMessage(PAGES_ACTIVITY_RATINGS_SERVICE.dateErrorMessage);

    return true;
  }, []);

  const ratingCheck = useCallback((divisions) => {
    if (divisions?.length <= 0) {
      setErrorMessage(PAGES_ACTIVITY_RATINGS_SERVICE.ratingErrorMessage);
      return true;
    }
    setErrorMessage(null);

    return false;
  }, []);

  useEffect(() => {
    if (error?.response?.status === 425) {
      navigate(DETAILS + "/" + id);
    }
  }, [error, navigate, id]);

  useEffect(() => {
    if (isTablet) {
      dispatch({ type: Actions.SET_SIZE_OF_PAGE, payload: 10 });
    } else if (isPC) {
      dispatch({ type: Actions.SET_SIZE_OF_PAGE, payload: 20 });
    }
    dispatch({ type: Actions.SET_PAGE, payload: 1 });
  }, [isTablet, isPC, dispatch]);

  useEffect(() => {
    const ratingFlag = ratingCheck(ratingState?.ratings);
    if (ratingFlag) return;

    if (ratingState.dateFiltration) {
      const dateFlag = dateCheck(ratingState.startDate, ratingState.endDate);
      if (dateFlag) return;
    }
    const { timeZone } = Intl.DateTimeFormat().resolvedOptions();
    const configData = {
      idActivity: id,
      page: ratingState.page,
      sizeOfPage: ratingState.sizeOfPage,
      ratings: ratingState.ratings,
      startDate: ratingState.dateFiltration ? ratingState.startDate : null,
      endDate: ratingState.dateFiltration ? ratingState.endDate : null,
      zoneID: timeZone,
    };

    let timerOfAvalibility;
    timerOfAvalibility = setTimeout(() => {
      setConfigParams(
        GET_RATING_PREVIEWS.url,
        GET_RATING_PREVIEWS.method,
        configData
      );
    }, 600);

    return () => {
      if (timerOfAvalibility) clearTimeout(timerOfAvalibility);
    };
  }, [id, ratingCheck, dateCheck, dispatch, ratingState, setConfigParams]);

  return {
    loading,
    data,
    error,
    errorMessage,
    pageHandler,
  };
};

export default useRatings;
