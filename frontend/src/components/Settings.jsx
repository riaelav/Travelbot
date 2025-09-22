import { useEffect, useState } from "react";

const DEFAULTS = {
  // Provider
  openaiKey: "",
  openaiModel: "chat-gpt 4.0",
  twilioSid: "",
  twilioAuth: "",
  twilioFrom: "",

  webhookUrl: "",

  // Bot behavior
  systemPrompt: "",
  bookingLink: "",
  priorityHighAt: 20,
  priorityLowAt: 5,
  abandonedMins: 60,

  // UI
  locale: "it-IT",
  theme: "dark",
};

const LS_KEY = "tb_settings";

export default function Settings() {
  const [cfg, setCfg] = useState(DEFAULTS);
  const [saved, setSaved] = useState(false);

  // carica da localStorage
  useEffect(() => {
    try {
      const raw = localStorage.getItem(LS_KEY);
      if (raw) setCfg({ ...DEFAULTS, ...JSON.parse(raw) });
    } catch {}
  }, []);

  const onChange = (k, v) => setCfg((s) => ({ ...s, [k]: v }));

  const onSave = () => {
    localStorage.setItem(LS_KEY, JSON.stringify(cfg));
    setSaved(true);
    setTimeout(() => setSaved(false), 1500);
  };

  const onClear = () => {
    localStorage.removeItem(LS_KEY);
    setCfg(DEFAULTS);
  };

  return (
    <div className="settings container-fluid p-0">
      <div className="card kpi-card p-3 mb-3">
        <div className="d-flex align-items-center">
          <h2 className="h6 m-0">Provider</h2>
          {saved && <span className="badge-pri mid ms-2">Saved</span>}
          <button className="btn btn-accent btn-sm ms-auto" onClick={onSave}>
            Save
          </button>
        </div>
        <div className="row g-3 mt-2">
          <div className="col-12 col-md-6">
            <label className="form-label">OpenAI API Key</label>
            <input
              type="password"
              className="form-control"
              value={cfg.openaiKey}
              onChange={(e) => onChange("openaiKey", e.target.value)}
              placeholder="sk-..."
            />
          </div>
          <div className="col-12 col-md-6">
            <label className="form-label">OpenAI Model</label>
            <input className="form-control" value={cfg.openaiModel} onChange={(e) => onChange("openaiModel", e.target.value)} />
          </div>
          <div className="col-12 col-md-4">
            <label className="form-label">Twilio SID</label>
            <input className="form-control" value={cfg.twilioSid} onChange={(e) => onChange("twilioSid", e.target.value)} />
          </div>
          <div className="col-12 col-md-4">
            <label className="form-label">Twilio Auth Token</label>
            <input type="password" className="form-control" value={cfg.twilioAuth} onChange={(e) => onChange("twilioAuth", e.target.value)} />
          </div>
          <div className="col-12 col-md-4">
            <label className="form-label">Twilio WhatsApp From</label>
            <input
              className="form-control"
              placeholder="whatsapp:+14155238886"
              value={cfg.twilioFrom}
              onChange={(e) => onChange("twilioFrom", e.target.value)}
            />
          </div>
          <div className="col-12">
            <label className="form-label">Webhook URL (ngrok → /twilio/inbound)</label>
            <input
              className="form-control"
              placeholder="https://xxxx.ngrok-free.app/twilio/inbound"
              value={cfg.webhookUrl}
              onChange={(e) => onChange("webhookUrl", e.target.value)}
            />
          </div>
        </div>
      </div>

      <div className="card kpi-card p-3 mb-3">
        <h2 className="h6 m-0 mb-2">Bot behavior</h2>
        <div className="row g-3">
          <div className="col-12">
            <label className="form-label">System prompt</label>
            <textarea rows={5} className="form-control" value={cfg.systemPrompt} onChange={(e) => onChange("systemPrompt", e.target.value)} />
          </div>
          <div className="col-12 col-md-6">
            <label className="form-label">Booking link</label>
            <input className="form-control" value={cfg.bookingLink} onChange={(e) => onChange("bookingLink", e.target.value)} />
          </div>
          <div className="col-6 col-md-2">
            <label className="form-label">High ≥ (msg)</label>
            <input type="number" className="form-control" value={cfg.priorityHighAt} onChange={(e) => onChange("priorityHighAt", +e.target.value)} />
          </div>
          <div className="col-6 col-md-2">
            <label className="form-label">Low &lt; (msg)</label>
            <input type="number" className="form-control" value={cfg.priorityLowAt} onChange={(e) => onChange("priorityLowAt", +e.target.value)} />
          </div>
          <div className="col-12 col-md-2">
            <label className="form-label">Abandoned (min)</label>
            <input type="number" className="form-control" value={cfg.abandonedMins} onChange={(e) => onChange("abandonedMins", +e.target.value)} />
          </div>
        </div>
      </div>

      <div className="card kpi-card p-3">
        <h2 className="h6 m-0 mb-2">Appearance & Data</h2>
        <div className="row g-3">
          <div className="col-6 col-md-3">
            <label className="form-label">Locale</label>
            <input className="form-control" value={cfg.locale} onChange={(e) => onChange("locale", e.target.value)} />
          </div>
          <div className="col-6 col-md-3">
            <label className="form-label">Theme</label>
            <select className="form-select" value={cfg.theme} onChange={(e) => onChange("theme", e.target.value)}>
              <option value="dark">Dark</option>
              <option value="light">Light</option>
            </select>
          </div>
          <div className="col-12 col-md-6 d-flex align-items-end justify-content-end">
            <button className="btn btn-outline-light me-2" onClick={onClear}>
              Clear local settings
            </button>
            <button className="btn btn-accent" onClick={onSave}>
              Save
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
