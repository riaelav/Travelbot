import { useEffect, useMemo, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { loadConversations, selectConversation } from "../redux/conversationsSlice";

const API = import.meta.env.VITE_API_BASE || "";

export default function Conversations() {
  const dispatch = useDispatch();
  const { items, status, error, selectedId } = useSelector((s) => s.conversations);

  const [drawerOpen, setDrawerOpen] = useState(false);
  const [activeConv, setActiveConv] = useState(null);
  const [messages, setMessages] = useState([]);
  const [loadingMsg, setLoadingMsg] = useState(false);
  const [errMsg, setErrMsg] = useState(null);

  useEffect(() => {
    if (status === "idle") dispatch(loadConversations());
  }, [status, dispatch]);

  const fmt = (d) => (d ? new Date(d).toLocaleString() : "—");
  const phoneOf = (c) => c?.customerPhone || c?.phone || c?.customer?.phone || "—";
  const countOf = (c) => c?.messagesCount ?? c?.messageCount ?? c?.stats?.messages ?? 0;

  // NEW: helper per lead/badge
  const leadOf = (c) => c?.leadValue || c?.lead || c?.priority || "MID";
  const leadBadge = (vRaw) => {
    const v = String(vRaw || "MID").toUpperCase();
    const cls = v === "HIGH" ? "high" : v === "LOW" ? "low" : "mid";
    const label = v[0] + v.slice(1).toLowerCase(); // High/Mid/Low
    return <span className={`badge-pri ${cls}`}>{label}</span>;
  };

  const openDrawer = async (conv) => {
    setActiveConv(conv);
    setDrawerOpen(true);
    setMessages([]);
    setErrMsg(null);
    setLoadingMsg(true);
    try {
      const token = localStorage.getItem("tb_token");
      const res = await fetch(`${API}/conversations/${conv.id}/messages`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error((await res.text()) || "Errore caricamento messaggi");
      const data = await res.json();
      setMessages(Array.isArray(data) ? data : []);
    } catch (e) {
      setErrMsg(String(e.message || e));
    } finally {
      setLoadingMsg(false);
    }
    dispatch(selectConversation(conv.id));
  };

  const closeDrawer = () => {
    setDrawerOpen(false);
    setActiveConv(null);
    setMessages([]);
  };

  const rows = useMemo(() => items, [items]);

  return (
    <div className="card kpi-card p-3">
      <div className="d-flex align-items-center mb-3">
        <h2 className="h5 m-0">Conversations</h2>
        <div className="ms-auto text-secondary small">{status === "loading" ? "Aggiornamento…" : `${rows.length} risultati`}</div>
      </div>

      {status === "failed" && <div className="text-danger mb-2">{String(error)}</div>}

      <div className="table-responsive table-shell">
        <table className="table table-dark-custom w-100">
          <thead>
            <tr>
              <th>ID</th>
              <th>Cliente</th>
              <th>Started at</th>
              <th>Closed at</th>
              <th># Msg</th>
              <th>Lead</th> {/* NEW */}
              <th>Azione</th>
            </tr>
          </thead>
          <tbody>
            {rows.map((c) => (
              <tr key={c.id} className={selectedId === c.id ? "active-row" : ""}>
                <td data-label="ID">{c.id}</td>
                <td data-label="Cliente">{phoneOf(c)}</td>
                <td data-label="Started at">{fmt(c.startedAt || c.createdAt)}</td>
                <td data-label="Closed at">{fmt(c.closedAt)}</td>
                <td data-label="# Msg">{countOf(c)}</td>
                <td data-label="Lead">{leadBadge(leadOf(c))}</td> {/* NEW */}
                <td data-label="Azione">
                  <button className="btn-reset sidebar-btn open-chat-btn" onClick={() => openDrawer(c)}>
                    <i className="bi bi-chat-dots me-1" />
                    Apri chat
                  </button>
                </td>
              </tr>
            ))}
            {rows.length === 0 && status === "succeeded" && (
              <tr>
                <td colSpan={7} className="text-secondary">
                  Nessuna conversazione
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Drawer laterale */}
      {drawerOpen && (
        <>
          <div className="drawer-backdrop" onClick={closeDrawer} />
          <aside className="drawer">
            <div className="d-flex align-items-center mb-2">
              <strong className="me-2">Chat</strong>
              <span className="badge-pri mid">{phoneOf(activeConv)}</span>
              <button className="btn btn-sm btn-outline-light ms-auto" onClick={closeDrawer}>
                <i className="bi bi-x"></i>
              </button>
            </div>
            <div className="small text-secondary mb-3">
              Started: {fmt(activeConv?.startedAt || activeConv?.createdAt)} • &nbsp;Closed: {fmt(activeConv?.closedAt)} • &nbsp;Msg: {countOf(activeConv)} •
              &nbsp;Lead: {leadOf(activeConv)}
            </div>

            <div className="chat-pane">
              {loadingMsg && <div className="text-secondary">Caricamento messaggi…</div>}
              {errMsg && <div className="text-danger mb-2">{errMsg}</div>}
              {!loadingMsg &&
                !errMsg &&
                messages.map((m) => (
                  <div key={m.id} className={`mb-2 ${m.role === "ASSISTANT" ? "text-end" : ""}`}>
                    <div className={`d-inline-block px-3 py-2 rounded-3 ${m.role === "ASSISTANT" ? "bg-assistant" : "bg-user"}`}>
                      <div className="small fw-semibold mb-1">{m.role === "ASSISTANT" ? "TravelBot" : "Cliente"}</div>
                      <div className="text-wrap">{m.body || m.content}</div>
                      <div className="small text-secondary mt-1">{fmt(m.createdAt)}</div>
                    </div>
                  </div>
                ))}
              {!loadingMsg && !errMsg && messages.length === 0 && <div className="text-secondary">Nessun messaggio</div>}
            </div>
          </aside>
        </>
      )}
    </div>
  );
}
