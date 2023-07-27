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
} from "../../state/slices/dislikedActivities";

import withAuth from "../../components/withAuth";
import Activities from "../../components/activities";

const DislikedActivities = () => {
  const dislikedActivitiesState = useSelector(
    (state) => state?.dislikedActivities?.value
  );

  return (
    <Activities
      state={dislikedActivitiesState}
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

export default withAuth(DislikedActivities);
