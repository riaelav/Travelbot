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
    <div className="card revolut p-3">
      <div className="d-flex align-items-center mb-3">
        <div className="ms-auto text-secondary small">{status === "loading" ? "Aggiornamento…" : `${rows.length} risultati`}</div>
      </div>

      {status === "failed" && <div className="text-danger mb-2">{String(error)}</div>}

      <div className="table-responsive">
        <table className="table table-dark-custom w-100">
          <thead>
            <tr>
              <th>ID</th>
              <th>Client</th>
              <th>Started at</th>
              <th>Closed at</th>
              <th>Messages</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {rows.map((c) => (
              <tr key={c.id} className={selectedId === c.id ? "active-row" : ""}>
                <td>{c.id}</td>
                <td>{phoneOf(c)}</td>
                <td>{fmt(c.startedAt || c.createdAt)}</td>
                <td>{fmt(c.closedAt)}</td>
                <td>{countOf(c)}</td>
                <td>
                  <button className="btn-reset sidebar-btn open-chat-btn" onClick={() => openDrawer(c)}>
                    Open chat
                  </button>
                </td>
              </tr>
            ))}
            {rows.length === 0 && status === "succeeded" && (
              <tr>
                <td colSpan={6} className="text-secondary">
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
              Started: {fmt(activeConv?.startedAt || activeConv?.createdAt)} • &nbsp;Closed: {fmt(activeConv?.closedAt)} • &nbsp;Msg: {countOf(activeConv)}
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
