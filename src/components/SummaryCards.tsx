import React from 'react';
import type { Player, Team } from '../types';

interface Props {
  teams: Team[];
  players: Player[];
  recommendationCount: number;
}

const SummaryCards: React.FC<Props> = ({ teams, players, recommendationCount }) => {
  const topTeam = [...teams].sort((a, b) => b.points - a.points)[0];
  const topBatsman = [...players].sort((a, b) => b.runs - a.runs)[0];
  const topBowler = [...players].filter((p) => p.wickets > 0).sort((a, b) => b.wickets - a.wickets)[0];
  const totalMatches = teams.reduce((s, t) => s + t.wins + t.losses + t.draws, 0) / 2;

  const cards = [
    { icon: '🏆', label: 'League Leader', value: topTeam.name, sub: `${topTeam.points} pts`, color: 'gold' },
    { icon: '🏏', label: 'Top Scorer', value: topBatsman.name, sub: `${topBatsman.runs} runs`, color: 'blue' },
    { icon: '🎳', label: 'Top Wicket-Taker', value: topBowler.name, sub: `${topBowler.wickets} wickets`, color: 'green' },
    { icon: '📊', label: 'Total Matches', value: String(Math.round(totalMatches)), sub: `${teams.length} teams`, color: 'purple' },
    { icon: '👥', label: 'Players Tracked', value: String(players.length), sub: 'across all teams', color: 'orange' },
    { icon: '💡', label: 'Recommendations', value: String(recommendationCount), sub: 'action items', color: 'red' },
  ];

  return (
    <div className="summary-grid">
      {cards.map((card) => (
        <div key={card.label} className={`summary-card summary-card--${card.color}`}>
          <div className="summary-icon">{card.icon}</div>
          <div className="summary-info">
            <p className="summary-label">{card.label}</p>
            <p className="summary-value">{card.value}</p>
            <p className="summary-sub">{card.sub}</p>
          </div>
        </div>
      ))}
    </div>
  );
};

export default SummaryCards;
