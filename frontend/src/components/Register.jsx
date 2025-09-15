import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { registerUser } from "../redux/authSlice";

export default function Register() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { status, error } = useSelector((s) => s.auth);

  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [showPwd, setShowPwd] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [localError, setLocalError] = useState(null);

  const onSubmit = async (e) => {
    e.preventDefault();
    setLocalError(null);
    if (!username.trim()) return setLocalError("Username richiesto.");
    if (username.length > 50) return setLocalError("Username max 50 caratteri.");
    if (password.length < 8) return setLocalError("Password minima 8 caratteri.");
    if (password !== confirm) return setLocalError("Le password non coincidono.");
    try {
      await dispatch(registerUser({ username, email, password })).unwrap();
      navigate("/login");
    } catch {}
  };

  return (
    <div className="auth-wrap">
      <div className="auth-card">
        <h1 className="auth-title">Create your account</h1>
        <p className="auth-sub">Use a username, a valid email and a strong password.</p>

        <form onSubmit={onSubmit} noValidate>
          <div className="mb-3">
            <label className="form-label">Username</label>
            <input
              className="form-control"
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              maxLength={50}
              placeholder="yourname"
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Email</label>
            <input className="form-control" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required placeholder="you@example.com" />
          </div>

          <div className="mb-3 position-relative">
            <label className="form-label">Password</label>
            <input
              className="form-control"
              type={showPwd ? "text" : "password"}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              minLength={8}
              placeholder="At least 8 characters"
            />
            <button type="button" className="auth-toggle" onClick={() => setShowPwd((s) => !s)}>
              <i className={`bi ${showPwd ? "bi-eye-slash" : "bi-eye"}`}></i>
            </button>
          </div>

          <div className="mb-2 position-relative">
            <label className="form-label">Confirm password</label>
            <input className="form-control" type={showConfirm ? "text" : "password"} value={confirm} onChange={(e) => setConfirm(e.target.value)} required />
            <button type="button" className="auth-toggle" onClick={() => setShowConfirm((s) => !s)}>
              <i className={`bi ${showConfirm ? "bi-eye-slash" : "bi-eye"}`}></i>
            </button>
          </div>

          {(localError || error) && <div className="auth-error mb-2">{localError || String(error)}</div>}

          <button className="btn btn-accent w-100" disabled={status === "loading"}>
            {status === "loading" ? "Creating…" : "Create account"}
          </button>
        </form>

        <div className="mt-3 auth-meta">
          Already have an account? <Link to="/login">Log in</Link>
        </div>
      </div>
    </div>
  );
}
