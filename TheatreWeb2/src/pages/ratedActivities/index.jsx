import React from "react";
import { useSelector } from "react-redux";

import {
  addActivityNameHandler,
  addAuthorNameHandler,
  addStartDateHandler,
  addEndDateHandler,
  addDivisionHandler,
  addPageHandler,
  addSizeOfPageHandler,
  setDateFiltration,
} from "../../state/slices/ratedActivities";

import withAuth from "../../components/withAuth";
import Activities from "../../components/activities";

const RatedActivities = () => {
  const ratedActivitiesState = useSelector(
    (state) => state?.ratedActivities?.value
  );

  return (
    <Activities
      state={ratedActivitiesState}
      addActivityNameHandler={addActivityNameHandler}
      addAuthorNameHandler={addAuthorNameHandler}
      addStartDateHandler={addStartDateHandler}
      addEndDateHandler={addEndDateHandler}
      addDivisionHandler={addDivisionHandler}
      addPageHandler={addPageHandler}
      addSizeOfPageHandler={addSizeOfPageHandler}
      setDateFiltration={setDateFiltration}
    />
  );
};

export default withAuth(RatedActivities);
