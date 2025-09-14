import { NavLink } from "react-router-dom";

export default function Sidebar({ open = false }) {
  return (
    <aside className={`sidebar ${open ? "open" : ""}`}>
      <div className="brand">
        <i className="bi bi-compass"></i>
        <span>Travelbot</span>
      </div>

      <div className="nav-group">
        <NavLink to="/" end className={({ isActive }) => `nav-item ${isActive ? "active" : ""}`}>
          <i className="bi bi-house"></i>
          <span>Home</span>
        </NavLink>
        <NavLink to="/conversations" className={({ isActive }) => `nav-item ${isActive ? "active" : ""}`}>
          <i className="bi bi-chat-dots"></i>
          <span>Conversations</span>
        </NavLink>
        <NavLink to="/analytics" className={({ isActive }) => `nav-item ${isActive ? "active" : ""}`}>
          <i className="bi bi-graph-up"></i>
          <span>Analytics</span>
        </NavLink>
        <NavLink to="/settings" className={({ isActive }) => `nav-item ${isActive ? "active" : ""}`}>
          <i className="bi bi-gear"></i>
          <span>Settings</span>
        </NavLink>
      </div>

      <div className="sidebar-footer">
        <div className="muted">v1.0 • Minimal</div>
      </div>
    </aside>
  );
}
