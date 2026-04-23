import React, { useCallback, useRef, useState } from 'react';
import type { VaptScan, ScanCategory, FindingSeverity } from '../types';
import { runMythosScan, SCAN_CATEGORY_LABELS, SEVERITY_ORDER } from '../utils/vaptEngine';

interface Props {
  scan: VaptScan;
  onUpdate: (updated: VaptScan) => void;
  onRemove: (id: string) => void;
}

const SEV_STYLE: Record<FindingSeverity, string> = {
  critical: 'sev-critical',
  high:     'sev-high',
  medium:   'sev-medium',
  low:      'sev-low',
  info:     'sev-info',
};

const SEV_ICON: Record<FindingSeverity, string> = {
  critical: '🚨',
  high:     '🔴',
  medium:   '🟠',
  low:      '🟡',
  info:     '🔵',
};

const MythosScanWindow: React.FC<Props> = ({ scan, onUpdate, onRemove }) => {
  const [expanded, setExpanded] = useState<Set<string>>(new Set());
  const cancelRef = useRef<(() => void) | null>(null);

  const update = useCallback(
    (patch: Partial<VaptScan>) => onUpdate({ ...scan, ...patch }),
    [scan, onUpdate]
  );

  const handleStart = () => {
    if (!scan.targetUrl.trim()) return;

    update({
      status: 'scanning',
      progress: 0,
      findings: [],
      mythosAnalysis: undefined,
      error: undefined,
      startedAt: new Date().toISOString(),
      completedAt: undefined,
    });

    cancelRef.current = runMythosScan(
      scan,
      (progress, partialFindings) => {
        onUpdate({ ...scan, status: 'scanning', progress, findings: partialFindings });
      },
      (findings, analysis) => {
        onUpdate({
          ...scan,
          status: 'complete',
          progress: 100,
          findings,
          mythosAnalysis: analysis,
          completedAt: new Date().toISOString(),
        });
        cancelRef.current = null;
      },
      (message) => {
        onUpdate({ ...scan, status: 'error', error: message });
        cancelRef.current = null;
      }
    );
  };

  const handleStop = () => {
    cancelRef.current?.();
    cancelRef.current = null;
    update({ status: 'idle', progress: 0 });
  };

  const toggleExpanded = (id: string) => {
    setExpanded((prev) => {
      const next = new Set(prev);
      next.has(id) ? next.delete(id) : next.add(id);
      return next;
    });
  };

  const sortedFindings = [...scan.findings].sort(
    (a, b) => SEVERITY_ORDER[a.severity] - SEVERITY_ORDER[b.severity]
  );

  const critCount = scan.findings.filter((f) => f.severity === 'critical').length;
  const highCount = scan.findings.filter((f) => f.severity === 'high').length;

  return (
    <div className={`msw-window ${scan.status === 'scanning' ? 'msw-window--scanning' : ''}`}>
      {/* Window Title Bar */}
      <div className="msw-titlebar">
        <div className="msw-titlebar-left">
          <span className="msw-logo">⬡</span>
          <span className="msw-title">Mythos Scan</span>
          <span className="msw-category-tag">{SCAN_CATEGORY_LABELS[scan.category]}</span>
        </div>
        <div className="msw-titlebar-right">
          {scan.status === 'complete' && (
            <span className={`msw-status-dot ${critCount > 0 ? 'dot-critical' : highCount > 0 ? 'dot-high' : 'dot-ok'}`} />
          )}
          {scan.status === 'scanning' && <span className="msw-status-dot dot-scanning" />}
          <button className="msw-close-btn" onClick={() => onRemove(scan.id)} title="Close window">✕</button>
        </div>
      </div>

      {/* Config Row */}
      <div className="msw-config-row">
        <input
          className="msw-url-input"
          placeholder="https://target-application.com"
          value={scan.targetUrl}
          disabled={scan.status === 'scanning'}
          onChange={(e) => update({ targetUrl: e.target.value })}
        />
        <select
          className="msw-category-select"
          value={scan.category}
          disabled={scan.status === 'scanning'}
          onChange={(e) => update({ category: e.target.value as ScanCategory })}
        >
          {Object.entries(SCAN_CATEGORY_LABELS).map(([val, label]) => (
            <option key={val} value={val}>{label}</option>
          ))}
        </select>
        <input
          className="msw-label-input"
          placeholder="Scan label…"
          value={scan.label}
          disabled={scan.status === 'scanning'}
          onChange={(e) => update({ label: e.target.value })}
        />
        {scan.status === 'scanning' ? (
          <button className="msw-btn msw-btn--stop" onClick={handleStop}>⏹ Stop</button>
        ) : (
          <button
            className="msw-btn msw-btn--start"
            onClick={handleStart}
            disabled={!scan.targetUrl.trim()}
          >
            ▶ Scan
          </button>
        )}
      </div>

      {/* Progress Bar */}
      {scan.status === 'scanning' && (
        <div className="msw-progress-row">
          <div className="msw-progress-track">
            <div className="msw-progress-bar" style={{ width: `${scan.progress}%` }} />
          </div>
          <span className="msw-progress-label">{scan.progress}%</span>
          <span className="msw-scanning-text">🔍 Mythos AI scanning…</span>
        </div>
      )}

      {/* Error */}
      {scan.status === 'error' && (
        <div className="msw-error-banner">⚠️ Scan error: {scan.error}</div>
      )}

      {/* Idle Placeholder */}
      {scan.status === 'idle' && (
        <div className="msw-idle-placeholder">
          <span className="msw-idle-icon">⬡</span>
          <p>Enter a target URL and click <strong>Scan</strong> to begin.</p>
          <p className="msw-idle-sub">Powered by <strong>Mythos AI</strong> — Claude Mythos deep-scan engine</p>
        </div>
      )}

      {/* Findings List */}
      {(scan.status === 'scanning' || scan.status === 'complete') && scan.findings.length > 0 && (
        <div className="msw-findings">
          <div className="msw-findings-header">
            <span className="msw-findings-title">Findings ({scan.findings.length})</span>
            <div className="msw-severity-summary">
              {critCount > 0 && <span className="sev-pill sev-critical">{critCount} Critical</span>}
              {highCount > 0 && <span className="sev-pill sev-high">{highCount} High</span>}
            </div>
          </div>
          {sortedFindings.map((finding) => (
            <div key={finding.id} className={`msw-finding ${SEV_STYLE[finding.severity]}`}>
              <button
                className="msw-finding-toggle"
                onClick={() => toggleExpanded(finding.id)}
              >
                <span className="msw-finding-sev">
                  {SEV_ICON[finding.severity]} <span className={`sev-text ${SEV_STYLE[finding.severity]}`}>{finding.severity.toUpperCase()}</span>
                </span>
                <span className="msw-finding-title">{finding.title}</span>
                <span className="msw-finding-endpoint">{finding.endpoint}</span>
                <span className="msw-toggle-icon">{expanded.has(finding.id) ? '▲' : '▼'}</span>
              </button>
              {expanded.has(finding.id) && (
                <div className="msw-finding-detail">
                  <div className="msw-finding-section">
                    <span className="msw-detail-label">Description</span>
                    <p>{finding.description}</p>
                  </div>
                  <div className="msw-finding-section">
                    <span className="msw-detail-label">Evidence</span>
                    <code className="msw-evidence">{finding.evidence}</code>
                  </div>
                  <div className="msw-finding-section">
                    <span className="msw-detail-label">Recommendation</span>
                    <p>{finding.recommendation}</p>
                  </div>
                  {finding.cve && (
                    <div className="msw-finding-section">
                      <span className="msw-detail-label">Reference</span>
                      <span className="msw-cve-tag">{finding.cve}</span>
                    </div>
                  )}
                </div>
              )}
            </div>
          ))}
        </div>
      )}

      {/* Mythos AI Analysis */}
      {scan.status === 'complete' && scan.mythosAnalysis && (
        <div className="msw-ai-analysis">
          <div className="msw-ai-header">
            <span className="msw-ai-icon">⬡</span>
            <span className="msw-ai-label">Mythos AI Analysis</span>
            {scan.completedAt && (
              <span className="msw-ai-time">
                {new Date(scan.completedAt).toLocaleTimeString()}
              </span>
            )}
          </div>
          <p className="msw-ai-text">{scan.mythosAnalysis}</p>
        </div>
      )}
    </div>
  );
};

export default MythosScanWindow;
