import React, { useState } from 'react';
import type { Recommendation } from '../types';

interface Props {
  recommendations: Recommendation[];
}

const priorityIcon: Record<string, string> = { high: '🔴', medium: '🟡', low: '🟢' };
const typeIcon: Record<string, string> = { player: '👤', team: '🏏', strategy: '📋' };

const Recommendations: React.FC<Props> = ({ recommendations }) => {
  const [activeFilter, setActiveFilter] = useState<'all' | 'high' | 'medium' | 'low'>('all');

  const counts = {
    all: recommendations.length,
    high: recommendations.filter((r) => r.priority === 'high').length,
    medium: recommendations.filter((r) => r.priority === 'medium').length,
    low: recommendations.filter((r) => r.priority === 'low').length,
  };

  const filtered = activeFilter === 'all' ? recommendations : recommendations.filter((r) => r.priority === activeFilter);

  return (
    <div className="card">
      <div className="card-header">
        <h2 className="section-title">Recommendations</h2>
        <div className="filter-tabs">
          {(['all', 'high', 'medium', 'low'] as const).map((f) => (
            <button
              key={f}
              className={`filter-tab filter-tab--${f} ${activeFilter === f ? 'active' : ''}`}
              onClick={() => setActiveFilter(f)}
            >
              {f.charAt(0).toUpperCase() + f.slice(1)}{' '}
              <span className="count-badge">{counts[f]}</span>
            </button>
          ))}
        </div>
      </div>

      {filtered.length === 0 ? (
        <p className="empty-state">No recommendations for this priority level.</p>
      ) : (
        <div className="rec-grid">
          {filtered.map((rec) => (
            <div key={rec.id} className={`rec-card rec-card--${rec.priority}`}>
              <div className="rec-header">
                <div className="rec-badges">
                  <span className={`priority-badge priority-badge--${rec.priority}`}>
                    {priorityIcon[rec.priority]} {rec.priority.toUpperCase()}
                  </span>
                  <span className="type-badge">
                    {typeIcon[rec.type]} {rec.type}
                  </span>
                </div>
                <div className="rec-metric">
                  <span className="metric-label">{rec.metric}</span>
                  <span className="metric-values">
                    <span className="metric-current">{rec.currentValue}</span>
                    <span className="metric-arrow">→</span>
                    <span className="metric-target">{rec.targetValue}</span>
                  </span>
                </div>
              </div>
              <h3 className="rec-title">{rec.title}</h3>
              <p className="rec-target">🎯 {rec.targetName}</p>
              <p className="rec-description">{rec.description}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Recommendations;
