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
} from "../../state/slices/likedActivities";

import withAuth from "../../components/withAuth";
import Activities from "../../components/activities";

const LikedActivities = () => {
  const likedActivitiesState = useSelector(
    (state) => state?.likedActivities?.value
  );

  return (
    <Activities
      state={likedActivitiesState}
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

export default withAuth(LikedActivities);
