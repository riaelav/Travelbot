import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { loadConversations } from "../redux/conversationsSlice";
import { PieChart, Pie, Cell, ResponsiveContainer } from "recharts";

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

  return (
    <div className="container-fluid p-0">
      <div className="row row-stretch g-3">
        {/* Donut */}
        <div className="col-12 col-lg-6">
          <div className="card chart-card p-3">
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

        {/* Priority + Funnel */}
        <div className="col-12 col-lg-6">
          <div className="card kpi-card p-3">
            <div className="d-flex align-items-center mb-2">
              <h2 className="h6 m-0">Priority</h2>
            </div>
            <div className="d-flex gap-3">
              <span className="badge-pri high">1 conversation high</span>
              <span className="badge-pri low">1 conversation low</span>
            </div>
            <div className="text-secondary small mt-2">Placeholder</div>
          </div>

          <div className="card kpi-card p-3 mt-3">
            <div className="d-flex align-items-center mb-2">
              <h2 className="h6 m-0">Funnel</h2>
            </div>
            <div className="d-flex gap-3">
              <span className="badge-pri mid">
                <i className="bi bi-check2-circle me-1" /> Conversions 2
              </span>
              <span className="badge-pri high">
                <i className="bi bi-cart-x me-1" /> Abandoned cart 1
              </span>
            </div>
            <div className="text-secondary small mt-2">Placeholder</div>
          </div>
        </div>
      </div>
    </div>
  );
}
