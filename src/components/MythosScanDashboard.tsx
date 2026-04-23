import React, { useCallback, useState } from 'react';
import type { VaptScan, ScanCategory } from '../types';
import MythosScanWindow from './MythosScanWindow';

let _scanCounter = 0;

function createScan(category: ScanCategory = 'full-scan'): VaptScan {
  return {
    id: `scan-${Date.now()}-${++_scanCounter}`,
    label: `Scan #${_scanCounter}`,
    targetUrl: '',
    category,
    status: 'idle',
    progress: 0,
    findings: [],
  };
}

const PRESETS: { label: string; category: ScanCategory }[] = [
  { label: '+ Full Scan', category: 'full-scan' },
  { label: '+ SQL Injection', category: 'sql-injection' },
  { label: '+ XSS', category: 'xss' },
  { label: '+ Auth', category: 'auth' },
  { label: '+ SSL/TLS', category: 'ssl-tls' },
  { label: '+ Headers', category: 'headers' },
];

const MythosScanDashboard: React.FC = () => {
  const [scans, setScans] = useState<VaptScan[]>([createScan('full-scan')]);

  const addScan = (category: ScanCategory) => {
    setScans((prev) => [...prev, createScan(category)]);
  };

  const updateScan = useCallback((updated: VaptScan) => {
    setScans((prev) => prev.map((s) => (s.id === updated.id ? updated : s)));
  }, []);

  const removeScan = useCallback((id: string) => {
    setScans((prev) => prev.filter((s) => s.id !== id));
  }, []);

  const totalFindings = scans.reduce((sum, s) => sum + s.findings.length, 0);
  const criticalCount = scans.reduce(
    (sum, s) => sum + s.findings.filter((f) => f.severity === 'critical').length,
    0
  );
  const activeScans = scans.filter((s) => s.status === 'scanning').length;
  const completedScans = scans.filter((s) => s.status === 'complete').length;

  return (
    <div className="msd-dashboard">
      {/* Dashboard Header */}
      <div className="msd-header">
        <div className="msd-brand">
          <span className="msd-brand-icon">⬡</span>
          <div>
            <h2 className="msd-brand-name">Mythos Scan</h2>
            <p className="msd-brand-sub">Powered by Claude Mythos AI · VAPT Engine v2.0</p>
          </div>
        </div>
        <div className="msd-stats">
          <div className="msd-stat">
            <span className="msd-stat-val">{scans.length}</span>
            <span className="msd-stat-lbl">Windows</span>
          </div>
          <div className="msd-stat">
            <span className="msd-stat-val msd-stat--active">{activeScans}</span>
            <span className="msd-stat-lbl">Scanning</span>
          </div>
          <div className="msd-stat">
            <span className="msd-stat-val">{completedScans}</span>
            <span className="msd-stat-lbl">Complete</span>
          </div>
          <div className="msd-stat">
            <span className="msd-stat-val">{totalFindings}</span>
            <span className="msd-stat-lbl">Findings</span>
          </div>
          {criticalCount > 0 && (
            <div className="msd-stat">
              <span className="msd-stat-val msd-stat--critical">{criticalCount}</span>
              <span className="msd-stat-lbl">Critical</span>
            </div>
          )}
        </div>
      </div>

      {/* Preset buttons */}
      <div className="msd-presets">
        <span className="msd-presets-label">Add scan window:</span>
        {PRESETS.map((p) => (
          <button
            key={p.category}
            className="msd-preset-btn"
            onClick={() => addScan(p.category)}
          >
            {p.label}
          </button>
        ))}
      </div>

      {/* Scan Windows Grid */}
      {scans.length === 0 ? (
        <div className="msd-empty">
          <span className="msd-empty-icon">⬡</span>
          <p>No scan windows open. Add a window using the buttons above.</p>
        </div>
      ) : (
        <div
          className={`msd-windows-grid ${
            scans.length === 1
              ? 'msd-grid--1'
              : scans.length === 2
              ? 'msd-grid--2'
              : 'msd-grid--3'
          }`}
        >
          {scans.map((scan) => (
            <MythosScanWindow
              key={scan.id}
              scan={scan}
              onUpdate={updateScan}
              onRemove={removeScan}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default MythosScanDashboard;
