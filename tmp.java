import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  spCount: 0,
  wpCount: 0,
};

const countSlice = createSlice({
  name: 'counts',
  initialState,
  reducers: {
    setSpCount: (state, action) => {
      state.spCount = action.payload;
    },
    setWpCount: (state, action) => {
      state.wpCount = action.payload;
    },
  },
});

export const { setSpCount, setWpCount } = countSlice.actions;
export default countSlice.reducer;
