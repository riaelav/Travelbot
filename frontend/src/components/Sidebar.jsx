import { NavLink, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../redux/authSlice";

export default function Sidebar({ open = false }) {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((s) => s.auth.user);

  const onLogout = () => {
    dispatch(logout());
    navigate("/login", { replace: true });
  };

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

        <NavLink to="/settings" className={({ isActive }) => `nav-item ${isActive ? "active" : ""}`}>
          <i className="bi bi-gear"></i>
          <span>Settings</span>
        </NavLink>
      </div>

      <div className="sidebar-footer">
        {user && (
          <div className="muted small mb-2">
            <i className="bi bi-person"></i> {user.email || user.username}
          </div>
        )}

        <a
          href="#"
          className="nav-item active"
          role="button"
          onClick={(e) => {
            e.preventDefault();
            dispatch(logout());
            navigate("/login", { replace: true });
          }}
        >
          <i className="bi bi-box-arrow-right"></i>
          <span>Logout</span>
        </a>
      </div>
    </aside>
  );
}
