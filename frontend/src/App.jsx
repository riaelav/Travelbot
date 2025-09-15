import { useState } from "react";
import { Routes, Route, Outlet, NavLink } from "react-router-dom";

import Sidebar from "./components/Sidebar.jsx";
import Home from "./components/Home.jsx";
import Conversations from "./components/Conversations.jsx";
import Analytics from "./components/Analytics.jsx";
import Settings from "./components/Settings.jsx";
import NotFound from "./components/NotFound.jsx";

import RequireAuth from "./components/RequireAuth.jsx";
import Login from "./components/Login.jsx";
import Register from "./components/Register.jsx";

function Layout() {
  const [open, setOpen] = useState(false);
  return (
    <>
      <Sidebar open={open} />
      <div className="topbar d-lg-none">
        <button className="btn btn-outline-light btn-sm" onClick={() => setOpen((x) => !x)}>
          <i className="bi bi-list"></i>
        </button>
        <strong>Travelbot Dashboard</strong>
        <NavLink to="/settings" className="ms-auto text-decoration-none text-light">
          <i className="bi bi-gear"></i>
        </NavLink>
      </div>
      <main className="content">
        <Outlet />
      </main>
    </>
  );
}

export default function App() {
  return (
    <Routes>
      {/* Pubbliche */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      {/* Protette */}
      <Route
        element={
          <RequireAuth>
            <Layout />
          </RequireAuth>
        }
      >
        <Route index element={<Home />} />
        <Route path="conversations" element={<Conversations />} />
        <Route path="analytics" element={<Analytics />} />
        <Route path="settings" element={<Settings />} />
      </Route>

      {/* 404 */}
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}
