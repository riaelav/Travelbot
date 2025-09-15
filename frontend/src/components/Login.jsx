import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { loginUser } from "../redux/authSlice";

export default function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const { status, error } = useSelector((s) => s.auth);

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPwd, setShowPwd] = useState(false);
  const [localError, setLocalError] = useState(null);

  const from = location.state?.from?.pathname || "/";

  const onSubmit = async (e) => {
    e.preventDefault();
    setLocalError(null);
    if (!email || !password) return setLocalError("Inserisci email e password.");
    try {
      await dispatch(loginUser({ email, password })).unwrap();
      navigate(from, { replace: true });
    } catch {}
  };

  return (
    <div className="auth-wrap">
      <div className="auth-card">
        <h1 className="auth-title">Welcome back</h1>
        <p className="auth-sub">Log in to access your dashboard.</p>

        <form onSubmit={onSubmit} noValidate>
          <div className="mb-3">
            <label className="form-label">Email</label>
            <input className="form-control" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          </div>

          <div className="mb-2 position-relative">
            <label className="form-label">Password</label>
            <input className="form-control" type={showPwd ? "text" : "password"} value={password} onChange={(e) => setPassword(e.target.value)} required />
            <button type="button" className="auth-toggle" onClick={() => setShowPwd((s) => !s)}>
              <i className={`bi ${showPwd ? "bi-eye-slash" : "bi-eye"}`} />
            </button>
          </div>

          {(localError || error) && <div className="auth-error mb-2">{localError || String(error)}</div>}

          <button className="btn btn-accent w-100" disabled={status === "loading"}>
            {status === "loading" ? "Signing in…" : "Login"}
          </button>
        </form>

        <div className="mt-3 auth-meta">
          New here? <Link to="/register">Create an account</Link>
        </div>
      </div>
    </div>
  );
}
