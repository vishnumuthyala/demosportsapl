export interface Player {
  id: number;
  name: string;
  team: string;
  position: string;
  matches: number;
  runs: number;
  wickets: number;
  catches: number;
  strikeRate: number;
  economy: number;
  average: number;
  recentForm: ('W' | 'L' | 'D')[];
}

export interface Team {
  id: number;
  name: string;
  wins: number;
  losses: number;
  draws: number;
  totalRuns: number;
  wicketsConceded: number;
  netRunRate: number;
  points: number;
}

export interface Recommendation {
  id: number;
  type: 'player' | 'team' | 'strategy';
  priority: 'high' | 'medium' | 'low';
  title: string;
  description: string;
  targetName: string;
  metric: string;
  currentValue: number | string;
  targetValue: number | string;
}

// ── Mythos Scan / VAPT ───────────────────────────────────────────

export type ScanStatus = 'idle' | 'scanning' | 'complete' | 'error';
export type FindingSeverity = 'critical' | 'high' | 'medium' | 'low' | 'info';
export type ScanCategory =
  | 'sql-injection'
  | 'xss'
  | 'csrf'
  | 'ssl-tls'
  | 'auth'
  | 'headers'
  | 'open-redirect'
  | 'file-inclusion'
  | 'full-scan';

export interface VaptFinding {
  id: string;
  severity: FindingSeverity;
  title: string;
  description: string;
  evidence: string;
  recommendation: string;
  cve?: string;
  endpoint: string;
}

export interface VaptScan {
  id: string;
  label: string;
  targetUrl: string;
  category: ScanCategory;
  status: ScanStatus;
  progress: number;           // 0–100
  findings: VaptFinding[];
  startedAt?: string;
  completedAt?: string;
  mythosAnalysis?: string;    // AI-generated summary
  error?: string;
}
