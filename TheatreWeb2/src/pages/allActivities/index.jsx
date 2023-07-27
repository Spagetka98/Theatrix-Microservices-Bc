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
} from "../../state/slices/allActivities";

import withAuth from "../../components/withAuth";
import Activities from "../../components/activities";

const AllActivities = () => {
  const allActivitiesState = useSelector(
    (state) => state?.allActivities?.value
  );

  return (
    <Activities
      showActionToolbar
      lastBox
      state={allActivitiesState}
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

export default withAuth(AllActivities);
