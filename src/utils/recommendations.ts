import type { Player, Team, Recommendation } from '../types';

export function generateRecommendations(players: Player[], teams: Team[]): Recommendation[] {
  const recommendations: Recommendation[] = [];
  let id = 1;

  // Player batting recommendations
  players.forEach((player) => {
    if ((player.position === 'Batsman' || player.position === 'All-Rounder') && player.average < 35) {
      recommendations.push({
        id: id++,
        type: 'player',
        priority: player.average < 25 ? 'high' : 'medium',
        title: 'Improve Batting Average',
        description: `${player.name}'s batting average is below the recommended threshold. Focus on defensive technique and shot selection to reduce dismissals.`,
        targetName: player.name,
        metric: 'Batting Average',
        currentValue: player.average,
        targetValue: 40,
      });
    }

    if ((player.position === 'Batsman' || player.position === 'All-Rounder') && player.strikeRate < 120) {
      recommendations.push({
        id: id++,
        type: 'player',
        priority: 'medium',
        title: 'Increase Strike Rate',
        description: `${player.name} needs to improve scoring speed. Incorporate power-hitting drills and work on rotating the strike more effectively.`,
        targetName: player.name,
        metric: 'Strike Rate',
        currentValue: player.strikeRate,
        targetValue: 130,
      });
    }

    if ((player.position === 'Bowler' || player.position === 'All-Rounder') && player.economy > 7.8) {
      recommendations.push({
        id: id++,
        type: 'player',
        priority: player.economy > 8.5 ? 'high' : 'medium',
        title: 'Reduce Economy Rate',
        description: `${player.name}'s economy rate is high. Work on line and length consistency to restrict run flow, especially in the death overs.`,
        targetName: player.name,
        metric: 'Economy Rate',
        currentValue: player.economy,
        targetValue: 7.5,
      });
    }

    // Poor recent form
    const recentLosses = player.recentForm.filter((r) => r === 'L').length;
    if (recentLosses >= 3) {
      recommendations.push({
        id: id++,
        type: 'player',
        priority: recentLosses >= 4 ? 'high' : 'medium',
        title: 'Address Poor Recent Form',
        description: `${player.name} has lost form recently (${recentLosses} losses in last 5 games). Consider additional practice sessions and mental conditioning support.`,
        targetName: player.name,
        metric: 'Recent Form',
        currentValue: `${recentLosses}/5 losses`,
        targetValue: '≤ 2/5 losses',
      });
    }
  });

  // Team recommendations
  teams.forEach((team) => {
    const winRate = team.wins / (team.wins + team.losses + team.draws);

    if (winRate < 0.5) {
      recommendations.push({
        id: id++,
        type: 'team',
        priority: winRate < 0.35 ? 'high' : 'medium',
        title: 'Improve Team Win Rate',
        description: `${team.name} has a win rate below 50%. Review team composition, batting order, and bowling strategy to turn results around.`,
        targetName: team.name,
        metric: 'Win Rate',
        currentValue: `${(winRate * 100).toFixed(1)}%`,
        targetValue: '≥ 60%',
      });
    }

    if (team.netRunRate < 0) {
      recommendations.push({
        id: id++,
        type: 'team',
        priority: 'high',
        title: 'Improve Net Run Rate',
        description: `${team.name} has a negative Net Run Rate (${team.netRunRate.toFixed(2)}). Focus on chasing targets more aggressively and defending totals better.`,
        targetName: team.name,
        metric: 'Net Run Rate',
        currentValue: team.netRunRate.toFixed(2),
        targetValue: '> 0.00',
      });
    }

    const avgRunsPerGame = team.totalRuns / (team.wins + team.losses + team.draws);
    if (avgRunsPerGame < 210) {
      recommendations.push({
        id: id++,
        type: 'strategy',
        priority: 'medium',
        title: 'Boost Team Batting Output',
        description: `${team.name} averages ${avgRunsPerGame.toFixed(0)} runs per game, which is below the competitive average. Consider promoting power hitters and optimizing batting order.`,
        targetName: team.name,
        metric: 'Avg Runs/Game',
        currentValue: avgRunsPerGame.toFixed(0),
        targetValue: '≥ 220',
      });
    }
  });

  // Sort by priority
  const priorityOrder = { high: 0, medium: 1, low: 2 };
  return recommendations.sort((a, b) => priorityOrder[a.priority] - priorityOrder[b.priority]);
}
