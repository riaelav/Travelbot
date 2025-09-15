import { configureStore } from "@reduxjs/toolkit";
import auth from "./authSlice";
import conversations from "./conversationsSlice";

export const store = configureStore({
  reducer: { auth, conversations },
});
