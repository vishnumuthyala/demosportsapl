import React, { useMemo, useState } from 'react';
import { players, teams } from './data/sportsData';
import { generateRecommendations } from './utils/recommendations';
import { exportToPDF } from './utils/pdfExport';
import SummaryCards from './components/SummaryCards';
import PointsTable from './components/PointsTable';
import PlayerStats from './components/PlayerStats';
import Recommendations from './components/Recommendations';
import MythosScanDashboard from './components/MythosScanDashboard';
import './App.css';

type Tab = 'dashboard' | 'players' | 'recommendations' | 'mythos-scan';

const App: React.FC = () => {
  const [activeTab, setActiveTab] = useState<Tab>('dashboard');
  const [exporting, setExporting] = useState(false);

  const recommendations = useMemo(() => generateRecommendations(players, teams), []);

  const handleExport = async () => {
    setExporting(true);
    try {
      exportToPDF(players, teams, recommendations);
    } finally {
      setExporting(false);
    }
  };

  return (
    <div className="app">
      {/* Header */}
      <header className={`app-header ${activeTab === 'mythos-scan' ? 'app-header--mythos' : ''}`}>
        <div className="header-inner">
          <div className="brand">
            {activeTab === 'mythos-scan' ? (
              <>
                <span className="brand-icon brand-icon--mythos">⬡</span>
                <div>
                  <h1 className="brand-name">Mythos Scan</h1>
                  <p className="brand-tagline">Claude Mythos AI · VAPT Security Scanner</p>
                </div>
              </>
            ) : (
              <>
                <span className="brand-icon">🏏</span>
                <div>
                  <h1 className="brand-name">Sports APL</h1>
                  <p className="brand-tagline">Performance Analytics Platform</p>
                </div>
              </>
            )}
          </div>
          {activeTab !== 'mythos-scan' && (
            <button className="export-btn" onClick={handleExport} disabled={exporting}>
              {exporting ? (
                <><span className="spinner" />Generating…</>
              ) : (
                <><span>📄</span> Export PDF Report</>
              )}
            </button>
          )}
        </div>
      </header>

      {/* Navigation */}
      <nav className={`tab-nav ${activeTab === 'mythos-scan' ? 'tab-nav--mythos' : ''}`}>
        <div className="tab-nav-inner">
          {(['dashboard', 'players', 'recommendations', 'mythos-scan'] as Tab[]).map((tab) => (
            <button
              key={tab}
              className={`tab-btn ${activeTab === tab ? 'active' : ''} ${tab === 'mythos-scan' ? 'tab-btn--mythos' : ''}`}
              onClick={() => setActiveTab(tab)}
            >
              {tab === 'dashboard' && '📊 '}
              {tab === 'players' && '👥 '}
              {tab === 'recommendations' && '💡 '}
              {tab === 'mythos-scan' && '⬡ '}
              {tab === 'mythos-scan'
                ? 'Mythos Scan'
                : tab.charAt(0).toUpperCase() + tab.slice(1)}
              {tab === 'recommendations' && (
                <span className="nav-badge">{recommendations.filter((r) => r.priority === 'high').length}</span>
              )}
            </button>
          ))}
        </div>
      </nav>

      {/* Main Content */}
      <main className="app-main">
        {activeTab === 'dashboard' && (
          <div className="tab-content">
            <SummaryCards teams={teams} players={players} recommendationCount={recommendations.length} />
            <PointsTable teams={teams} />
          </div>
        )}
        {activeTab === 'players' && (
          <div className="tab-content">
            <PlayerStats players={players} />
          </div>
        )}
        {activeTab === 'recommendations' && (
          <div className="tab-content">
            <Recommendations recommendations={recommendations} />
          </div>
        )}
        {activeTab === 'mythos-scan' && (
          <div className="tab-content tab-content--full">
            <MythosScanDashboard />
          </div>
        )}
      </main>

      <footer className="app-footer">
        {activeTab === 'mythos-scan' ? (
          <p>© {new Date().getFullYear()} Mythos Scan · Claude Mythos AI Engine · For authorised testing only</p>
        ) : (
          <p>© {new Date().getFullYear()} Sports APL · Performance Analytics Platform</p>
        )}
      </footer>
    </div>
  );
};

export default App;
