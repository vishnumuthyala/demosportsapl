import React, { useMemo, useState } from 'react';
import { players, teams } from './data/sportsData';
import { generateRecommendations } from './utils/recommendations';
import { exportToPDF } from './utils/pdfExport';
import SummaryCards from './components/SummaryCards';
import PointsTable from './components/PointsTable';
import PlayerStats from './components/PlayerStats';
import Recommendations from './components/Recommendations';
import './App.css';

type Tab = 'dashboard' | 'players' | 'recommendations';

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
      <header className="app-header">
        <div className="header-inner">
          <div className="brand">
            <span className="brand-icon">🏏</span>
            <div>
              <h1 className="brand-name">Sports APL</h1>
              <p className="brand-tagline">Performance Analytics Platform</p>
            </div>
          </div>
          <button className="export-btn" onClick={handleExport} disabled={exporting}>
            {exporting ? (
              <><span className="spinner" />Generating…</>
            ) : (
              <><span>📄</span> Export PDF Report</>
            )}
          </button>
        </div>
      </header>

      {/* Navigation */}
      <nav className="tab-nav">
        <div className="tab-nav-inner">
          {(['dashboard', 'players', 'recommendations'] as Tab[]).map((tab) => (
            <button
              key={tab}
              className={`tab-btn ${activeTab === tab ? 'active' : ''}`}
              onClick={() => setActiveTab(tab)}
            >
              {tab === 'dashboard' && '📊 '}
              {tab === 'players' && '👥 '}
              {tab === 'recommendations' && '💡 '}
              {tab.charAt(0).toUpperCase() + tab.slice(1)}
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
      </main>

      <footer className="app-footer">
        <p>© {new Date().getFullYear()} Sports APL · Performance Analytics Platform</p>
      </footer>
    </div>
  );
};

export default App;
