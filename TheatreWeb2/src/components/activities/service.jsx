import { useCallback, useState, useEffect } from "react";
import { useMediaQuery } from "react-responsive";
import { useDispatch } from "react-redux";
import moment from "moment";

import useAxios from "../../hooks/useAxios";

import { COMP_ACTIVITIES_SERVICE } from "../../config/texts";
import { TABLET, FULL_HD } from "../../config/sizes";
import { GET_ALL_PREVIEWS_PAGE } from "../../config/axios/endpoints";

const useActivities = (
  state,
  lastBox,
  addSizeOfPageHandler,
  addPageHandler
) => {
  const dispatch = useDispatch();
  const [data, error, loading, setConfigParams] = useAxios();
  const [paramsError, setParamsError] = useState(null);
  const [mobileMode, setMobileMode] = useState(true);

  const isTablet = useMediaQuery({
    query: TABLET,
  });
  const isPC = useMediaQuery({
    query: FULL_HD,
  });

  const pageHandler = (event, value) => {
    dispatch(addPageHandler(value));
  };

  const dateCheck = useCallback((startDate, endDate) => {
    const dateStart = moment(startDate, "DD-MM-YYYY").toDate();
    const dateEnd = moment(endDate, "DD-MM-YYYY").toDate();

    if (
      dateStart <= dateEnd ||
      (dateStart && dateEnd == null) ||
      (dateStart == null && dateEnd)
    ) {
      setParamsError(null);
      return false;
    }
    setParamsError(COMP_ACTIVITIES_SERVICE.dateError);
    return true;
  }, []);

  const divisionCheck = useCallback((divisions) => {
    if (divisions && divisions.length > 0) {
      setParamsError(null);
      return false;
    } else {
      setParamsError(COMP_ACTIVITIES_SERVICE.categoriesError);
      return true;
    }
  }, []);

  const mobileModeHandler = useCallback((status) => {
    setMobileMode(status);
  }, []);

  useEffect(() => {
    const dateStart = state?.startDate;
    const endDate = state?.endDate;
    const divisions = state?.divisions;
    const { timeZone } = Intl.DateTimeFormat().resolvedOptions();

    if (state.dateFiltration) {
      const dateFlag = dateCheck(dateStart, endDate);
      if (dateFlag) return;
    }

    const divisionFlag = divisionCheck(divisions);
    if (divisionFlag) return;

    const configData = {
      ...state,
      startDate: state.dateFiltration ? state.startDate : null,
      endDate: state.dateFiltration ? state.endDate : null,
      zoneID: timeZone,
    };
    delete configData.dateFiltration;

    let timerOfAvalibility;
    timerOfAvalibility = setTimeout(() => {
      setConfigParams(
        GET_ALL_PREVIEWS_PAGE.url,
        GET_ALL_PREVIEWS_PAGE.method,
        configData
      );
    }, 550);

    return () => {
      if (timerOfAvalibility) clearTimeout(timerOfAvalibility);
    };
  }, [state, dateCheck, divisionCheck, setConfigParams]);

  useEffect(() => {
    if (isTablet) {
      dispatch(addSizeOfPageHandler(lastBox ? 9 : 10));
    } else if (isPC) {
      dispatch(addSizeOfPageHandler(lastBox ? 16 : 17));
    }
  }, [isTablet, isPC, lastBox, dispatch, addSizeOfPageHandler]);

  return {
    paramsError,
    loading,
    error,
    data,
    mobileMode,
    pageHandler,
    mobileModeHandler,
  };
};

export default useActivities;
