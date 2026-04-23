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
