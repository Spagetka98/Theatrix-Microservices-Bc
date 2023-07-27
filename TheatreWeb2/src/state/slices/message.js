import { createSlice } from "@reduxjs/toolkit";

const MESSAGE = "message";

const initialStateValue = { isError: false, msg: null };

export const messageSlice = createSlice({
  name: MESSAGE,
  initialState: { value: initialStateValue },
  reducers: {
    setMessage: (state, action) => {
      state.value.isError = action.payload.isError;
      state.value.msg = action.payload.msg;
    },
    deleteMessage: (state) => {
      state.value.isError = null;
      state.value.msg = null;
    },
  },
});

export const { setMessage, deleteMessage } = messageSlice.actions;

export default messageSlice.reducer;
