import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { loadConversations } from "../redux/conversationsSlice";
import { PieChart, Pie, Cell, ResponsiveContainer } from "recharts";
import Conversations from "./Conversations.jsx";

export default function Home() {
  const dispatch = useDispatch();
  const { items, status } = useSelector((s) => s.conversations);

  useEffect(() => {
    if (status === "idle") dispatch(loadConversations());
  }, [status, dispatch]);

  // 1 min risparmiato per messaggio
  const savedMinutes = items.reduce((sum, c) => sum + (c.messagesCount ?? c.messageCount ?? 0), 0);
  const goalMinutes = Math.max(480, Math.ceil(savedMinutes / 60) * 60);
  const data = [
    { name: "Saved", value: Math.min(savedMinutes, goalMinutes) },
    { name: "Remaining", value: Math.max(goalMinutes - savedMinutes, 0) },
  ];
  const savedHours = Math.floor(savedMinutes / 60);
  const savedRemMin = savedMinutes % 60;

  // conteggio dinamico LOW/MID/HIGH
  const counts = items.reduce(
    (acc, c) => {
      const v = String(c?.leadValue || c?.lead || c?.priority || "").toUpperCase();
      if (v === "HIGH") acc.HIGH += 1;
      if (v === "LOW") acc.LOW += 1;
      return acc;
    },
    { LOW: 0, HIGH: 0 }
  );
  return (
    <div className="container-fluid p-0">
      <div className="row row-stretch g-3">
        {/* Colonna sinistra: Donut */}
        <div className="col-12 col-lg-6 d-flex">
          <div className="card chart-card p-3 h-100 w-100">
            <div className="chart-header">
              <h2 className="h6 m-0">Time saved</h2>
              <span className="badge-pri mid">{savedMinutes} min</span>
            </div>
            <div className="chart-body">
              <div style={{ width: "100%", height: 260, position: "relative" }}>
                <ResponsiveContainer>
                  <PieChart>
                    <Pie data={data} dataKey="value" innerRadius={70} outerRadius={100} stroke="none" startAngle={90} endAngle={-270}>
                      <Cell fill="var(--violet)" />
                      <Cell fill="rgba(255,255,255,0.08)" />
                    </Pie>
                  </PieChart>
                </ResponsiveContainer>
                <div className="donut-center">
                  <div className="value">
                    {savedHours}h {savedRemMin}m
                  </div>
                  <div className="hint">1 min per message</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Colonna destra: Priority + Funnel */}
        <div className="col-12 col-lg-6 d-flex">
          <div className="d-flex flex-column gap-3 w-100 h-100">
            <div className="card kpi-card p-3 flex-fill">
              <div className="d-flex align-items-center mb-2">
                <h2 className="h6 m-0">Priority</h2>
              </div>
              <div className="d-flex gap-3 flex-nowrap align-items-center badge-row">
                <span className="badge-pri high">{counts.HIGH} conversations high</span>
                <span className="badge-pri low">{counts.LOW} conversations low</span>
              </div>
            </div>

            {/* Funnel */}
            <div className="card kpi-card p-3 flex-fill">
              <div className="d-flex align-items-center mb-2">
                <h2 className="h6 m-0">Funnel</h2>
              </div>
              <div className="d-flex gap-3 flex-nowrap align-items-center badge-row">
                <span className="badge-pri mid">
                  <i className="bi bi-check2-circle me-1" /> Conversions 2
                </span>
                <span className="badge-pri high">
                  <i className="bi bi-cart-x me-1" /> Abandoned cart 1
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Sezione conversazioni */}
      <div className="mt-4">
        <Conversations />
      </div>
    </div>
  );
}
