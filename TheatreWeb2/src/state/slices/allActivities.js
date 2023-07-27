import { createSlice } from "@reduxjs/toolkit";
import moment from "moment";

const ALL_ACTIVITIES = "AllActivities";

const initialStateValue = {
  page: 0,
  sizeOfPage: 10,
  activityName: null,
  authorName: null,
  startDate: null,
  endDate: null,
  divisions: null,
  dateFiltration: false,
};

export const allActivitiesSlice = createSlice({
  name: ALL_ACTIVITIES,
  initialState: { value: initialStateValue },
  reducers: {
    addActivityNameHandler: (state, action) => {
      state.value.activityName = action.payload;
    },
    addAuthorNameHandler: (state, action) => {
      state.value.authorName = action.payload;
    },
    addStartDateHandler: (state, action) => {
      state.value.startDate = moment(new Date(action.payload)).format(
        "DD-MM-YYYY"
      );
    },
    addEndDateHandler: (state, action) => {
      state.value.endDate = moment(new Date(action.payload)).format(
        "DD-MM-YYYY"
      );
    },
    addDivisionHandler: (state, action) => {
      state.value.divisions = action.payload;
    },
    addPageHandler: (state, action) => {
      state.value.page = action.payload - 1;
    },
    addSizeOfPageHandler: (state, action) => {
      state.value.sizeOfPage = action.payload;
    },
    setDateFiltration: (state, action) => {
      state.value.dateFiltration = action.payload;
    },
    clearAllActivitiesFiltters: (state) => {
      state.value = initialStateValue;
    },
  },
});

export const {
  addActivityNameHandler,
  addAuthorNameHandler,
  addStartDateHandler,
  addEndDateHandler,
  addDivisionHandler,
  addPageHandler,
  addSizeOfPageHandler,
  setDateFiltration,
  clearAllActivitiesFiltters,
} = allActivitiesSlice.actions;

export default allActivitiesSlice.reducer;
