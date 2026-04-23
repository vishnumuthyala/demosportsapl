import React from 'react';
import type { Team } from '../types';

interface Props {
  teams: Team[];
}

const PointsTable: React.FC<Props> = ({ teams }) => {
  const sorted = [...teams].sort((a, b) => b.points - a.points);
  return (
    <div className="card">
      <h2 className="section-title">Points Table</h2>
      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Team</th>
              <th>W</th>
              <th>L</th>
              <th>D</th>
              <th>Pts</th>
              <th>NRR</th>
            </tr>
          </thead>
          <tbody>
            {sorted.map((team, index) => (
              <tr key={team.id} className={index < 4 ? 'qualify-row' : ''}>
                <td className="rank">{index + 1}</td>
                <td className="team-name">{team.name}</td>
                <td>{team.wins}</td>
                <td>{team.losses}</td>
                <td>{team.draws}</td>
                <td className="points">{team.points}</td>
                <td className={team.netRunRate >= 0 ? 'positive' : 'negative'}>
                  {team.netRunRate >= 0 ? '+' : ''}{team.netRunRate.toFixed(2)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <p className="table-note">✦ Top 4 teams qualify for playoffs</p>
    </div>
  );
};

export default PointsTable;
