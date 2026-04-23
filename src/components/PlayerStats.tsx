import React, { useState } from 'react';
import type { Player } from '../types';

interface Props {
  players: Player[];
}

type SortKey = keyof Pick<Player, 'name' | 'team' | 'matches' | 'runs' | 'average' | 'strikeRate' | 'wickets' | 'economy'>;

const formColor = (result: 'W' | 'L' | 'D') => {
  if (result === 'W') return 'form-win';
  if (result === 'L') return 'form-loss';
  return 'form-draw';
};

const PlayerStats: React.FC<Props> = ({ players }) => {
  const [sortKey, setSortKey] = useState<SortKey>('runs');
  const [sortAsc, setSortAsc] = useState(false);
  const [filter, setFilter] = useState('');

  const handleSort = (key: SortKey) => {
    if (key === sortKey) setSortAsc(!sortAsc);
    else { setSortKey(key); setSortAsc(false); }
  };

  const sorted = [...players]
    .filter((p) => p.name.toLowerCase().includes(filter.toLowerCase()) || p.team.toLowerCase().includes(filter.toLowerCase()))
    .sort((a, b) => {
      const av = a[sortKey];
      const bv = b[sortKey];
      const cmp = typeof av === 'number' && typeof bv === 'number' ? av - bv : String(av).localeCompare(String(bv));
      return sortAsc ? cmp : -cmp;
    });

  const SortIcon = ({ k }: { k: SortKey }) =>
    sortKey === k ? (sortAsc ? <span>▲</span> : <span>▼</span>) : <span className="sort-placeholder">⇅</span>;

  return (
    <div className="card">
      <div className="card-header">
        <h2 className="section-title">Player Statistics</h2>
        <input
          className="search-input"
          placeholder="Search player or team…"
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
        />
      </div>
      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              {(['name', 'team', 'matches', 'runs', 'average', 'strikeRate', 'wickets', 'economy'] as SortKey[]).map((k) => (
                <th key={k} onClick={() => handleSort(k)} className="sortable">
                  {k === 'strikeRate' ? 'SR' : k === 'economy' ? 'Eco' : k === 'average' ? 'Avg' : k === 'matches' ? 'M' : k.charAt(0).toUpperCase() + k.slice(1)}
                  {' '}<SortIcon k={k} />
                </th>
              ))}
              <th>Form</th>
            </tr>
          </thead>
          <tbody>
            {sorted.map((p) => (
              <tr key={p.id}>
                <td className="player-name">{p.name}</td>
                <td><span className="team-badge">{p.team}</span></td>
                <td>{p.matches}</td>
                <td className="highlight">{p.runs}</td>
                <td>{p.average.toFixed(1)}</td>
                <td>{p.strikeRate.toFixed(1)}</td>
                <td className="highlight">{p.wickets}</td>
                <td>{p.economy > 0 ? p.economy.toFixed(1) : '—'}</td>
                <td>
                  <div className="form-row">
                    {p.recentForm.map((r, i) => (
                      <span key={i} className={`form-dot ${formColor(r)}`}>{r}</span>
                    ))}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default PlayerStats;
