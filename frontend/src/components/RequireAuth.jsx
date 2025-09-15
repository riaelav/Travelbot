import { useSelector } from "react-redux";
import { Navigate, useLocation } from "react-router-dom";

export default function RequireAuth({ children }) {
  const token = useSelector((s) => s.auth?.token);
  const location = useLocation();

  if (!token) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  return children;
}
