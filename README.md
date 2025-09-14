# Travelbot — WhatsApp AI Travel Assistant & Dashboard

**Travelbot** is an end-to-end project composed of:

- **Backend:** Java Spring Boot to receive WhatsApp messages, talk to OpenAI, and persist conversations in PostgreSQL.
- **Frontend:** React + Vite with a dark, minimal dashboard to browse conversations, KPIs, and settings. Global state handled with Redux.

## ✨ Features

- WhatsApp webhook `POST /twilio/inbound`
- Persistence for `Customer` / `Conversation` / `Message`
- OpenAI call (model `gpt-4o-mini`) using the “TravelBot” prompt
- Sending replies to users via Twilio REST API
- Prompt design: gentle and non-intrusive tone; always 3 proposals (Budget / Standard / Premium). If the user provides a budget, append a configurable booking link at the end.
- Dashboard: routing with React Router, dark styling, Bootstrap/React-Bootstrap components, Redux for conversations and selected chat state

---

**Travelbot** è un progetto end-to-end composto da:

- **Backend**: Java Spring Boot per ricevere messaggi WhatsApp, parlare con OpenAI e salvare le conversazioni su PostgreSQL.
- **Frontend**: React + Vite con dashboard dark minimal per consultare conversazioni, KPI e impostazioni. Stato globale con Redux.

## ✨ Funzionalità

- Webhook WhatsApp `POST /twilio/inbound`
- Persistenza `Customer` / `Conversation` / `Message`.
- Chiamata a OpenAI (modello `gpt-4o-mini`) con prompt “TravelBot”.
- Invio risposta all’utente via Twilio REST API.
- Prompt design: tono gentile e non invadente, sempre 3 proposte (Budget/Standard/Premium), se l’utente indica un budget, in fondo aggiungi un link prenotazione configurabile.
- Dashboard: routing con React Router, stile dark, componenti Bootstrap/React-Bootstrap, Redux per conversazioni e selezione chat.
