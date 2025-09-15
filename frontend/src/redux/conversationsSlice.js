import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

const API = import.meta.env.VITE_API_BASE || "";

export const loadConversations = createAsyncThunk("conversations/load", async () => {
  const token = localStorage.getItem("tb_token");
  const res = await fetch(`${API}/api/conversations`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error((await res.text()) || "Fetch failed");
  return await res.json();
});

const slice = createSlice({
  name: "conversations",
  initialState: { items: [], status: "idle", error: null, selectedId: null },
  reducers: {
    selectConversation(state, action) {
      state.selectedId = action.payload;
    },
    upsertConversation(state, action) {
      const conv = action.payload;
      const i = state.items.findIndex((c) => c.id === conv.id);
      if (i >= 0) state.items[i] = conv;
      else state.items.unshift(conv);
    },
  },
  extraReducers: (b) => {
    b.addCase(loadConversations.pending, (s) => {
      s.status = "loading";
      s.error = null;
    })
      .addCase(loadConversations.fulfilled, (s, a) => {
        s.status = "succeeded";
        s.items = a.payload || [];
      })
      .addCase(loadConversations.rejected, (s, a) => {
        s.status = "failed";
        s.error = a.error.message;
      });
  },
});

export const { selectConversation, upsertConversation } = slice.actions;
export default slice.reducer;
