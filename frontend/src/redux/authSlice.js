import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

const API = import.meta.env.VITE_API_BASE || "";

export const registerUser = createAsyncThunk("auth/register", async ({ username, email, password }) => {
  const res = await fetch(`${API}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, email, password }),
    credentials: "include",
  });
  if (!res.ok) throw new Error((await res.text()) || "Registration failed");
  try {
    return await res.json();
  } catch {
    return { ok: true };
  }
});

export const loginUser = createAsyncThunk("auth/login", async ({ email, password }) => {
  const res = await fetch(`${API}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
    credentials: "include",
  });
  if (!res.ok) throw new Error((await res.text()) || "Invalid credentials");

  const data = await res.json();
  const token = data.accessToken || data.token;
  const user = data.user || null;

  localStorage.setItem("tb_token", token);
  if (user) localStorage.setItem("tb_user", JSON.stringify(user));

  return { token, user };
});

const initialToken = localStorage.getItem("tb_token");
const initialUser = (() => {
  try {
    return JSON.parse(localStorage.getItem("tb_user"));
  } catch {
    return null;
  }
})();

const slice = createSlice({
  name: "auth",
  initialState: { token: initialToken, user: initialUser, status: "idle", error: null },
  reducers: {
    logout(state) {
      state.token = null;
      state.user = null;
      localStorage.removeItem("tb_token");
      localStorage.removeItem("tb_user");
    },
  },
  extraReducers: (b) => {
    b.addCase(registerUser.pending, (s) => {
      s.status = "loading";
      s.error = null;
    })
      .addCase(registerUser.fulfilled, (s) => {
        s.status = "succeeded";
      })
      .addCase(registerUser.rejected, (s, a) => {
        s.status = "failed";
        s.error = a.error.message;
      })

      .addCase(loginUser.pending, (s) => {
        s.status = "loading";
        s.error = null;
      })
      .addCase(loginUser.fulfilled, (s, a) => {
        s.status = "succeeded";
        s.token = a.payload.token;
        s.user = a.payload.user;
      })
      .addCase(loginUser.rejected, (s, a) => {
        s.status = "failed";
        s.error = a.error.message;
      });
  },
});

export const { logout } = slice.actions;
export default slice.reducer;
