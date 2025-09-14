export default function Home() {
  return (
    <div className="container-fluid g-0">
      <div className="row g-3">
        <div className="col-12 col-lg-6">
          <div className="card revolut p-3">
            <div className="d-flex justify-content-between align-items-center">
              <h2 className="h5 m-0">Overview</h2>
              <button className="btn btn-accent btn-sm">Nuovo report</button>
            </div>
            <p className="text-secondary mt-2 mb-0">Messaggi, conversazioni e lead in evidenza.</p>
          </div>
        </div>
        <div className="col-12 col-lg-6">
          <div className="card revolut p-3">
            <h2 className="h5 m-0">Quick actions</h2>
            <ul className="m-0 mt-2">
              <li>Importa conversazioni</li>
              <li>Aggiungi tag / priorità</li>
              <li>Esporta CSV</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}
