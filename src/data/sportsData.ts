import type { Player, Team } from '../types';

export const teams: Team[] = [
  { id: 1, name: 'Thunder Hawks', wins: 10, losses: 3, draws: 1, totalRuns: 3420, wicketsConceded: 112, netRunRate: 1.24, points: 21 },
  { id: 2, name: 'Royal Strikers', wins: 9, losses: 4, draws: 1, totalRuns: 3210, wicketsConceded: 128, netRunRate: 0.95, points: 19 },
  { id: 3, name: 'Blue Tigers', wins: 8, losses: 4, draws: 2, totalRuns: 3180, wicketsConceded: 134, netRunRate: 0.78, points: 18 },
  { id: 4, name: 'Golden Eagles', wins: 7, losses: 6, draws: 1, totalRuns: 3050, wicketsConceded: 140, netRunRate: 0.45, points: 15 },
  { id: 5, name: 'Red Wolves', wins: 6, losses: 7, draws: 1, totalRuns: 2980, wicketsConceded: 152, netRunRate: 0.12, points: 13 },
  { id: 6, name: 'Silver Foxes', wins: 5, losses: 7, draws: 2, totalRuns: 2900, wicketsConceded: 158, netRunRate: -0.22, points: 12 },
  { id: 7, name: 'Black Panthers', wins: 4, losses: 9, draws: 1, totalRuns: 2760, wicketsConceded: 165, netRunRate: -0.55, points: 9 },
  { id: 8, name: 'White Sharks', wins: 2, losses: 11, draws: 1, totalRuns: 2580, wicketsConceded: 178, netRunRate: -1.18, points: 5 },
];

export const players: Player[] = [
  { id: 1, name: 'Arjun Sharma', team: 'Thunder Hawks', position: 'Batsman', matches: 14, runs: 680, wickets: 2, catches: 8, strikeRate: 142.5, economy: 8.2, average: 56.7, recentForm: ['W', 'W', 'W', 'L', 'W'] },
  { id: 2, name: 'Priya Patel', team: 'Royal Strikers', position: 'All-Rounder', matches: 14, runs: 520, wickets: 18, catches: 6, strikeRate: 128.4, economy: 7.1, average: 43.3, recentForm: ['W', 'L', 'W', 'W', 'W'] },
  { id: 3, name: 'Ravi Kumar', team: 'Blue Tigers', position: 'Bowler', matches: 14, runs: 145, wickets: 26, catches: 4, strikeRate: 95.6, economy: 6.4, average: 14.5, recentForm: ['W', 'W', 'L', 'W', 'L'] },
  { id: 4, name: 'Sneha Reddy', team: 'Thunder Hawks', position: 'Batsman', matches: 13, runs: 610, wickets: 0, catches: 12, strikeRate: 138.2, economy: 0, average: 50.8, recentForm: ['L', 'W', 'W', 'W', 'W'] },
  { id: 5, name: 'Kiran Mehta', team: 'Golden Eagles', position: 'Wicket-Keeper', matches: 14, runs: 430, wickets: 0, catches: 22, strikeRate: 122.7, economy: 0, average: 38.6, recentForm: ['L', 'L', 'W', 'L', 'W'] },
  { id: 6, name: 'Amit Singh', team: 'Royal Strikers', position: 'Bowler', matches: 13, runs: 78, wickets: 24, catches: 3, strikeRate: 88.4, economy: 6.8, average: 11.2, recentForm: ['W', 'W', 'W', 'L', 'W'] },
  { id: 7, name: 'Divya Nair', team: 'Blue Tigers', position: 'All-Rounder', matches: 14, runs: 390, wickets: 14, catches: 7, strikeRate: 115.8, economy: 7.5, average: 32.5, recentForm: ['W', 'L', 'D', 'W', 'L'] },
  { id: 8, name: 'Suresh Verma', team: 'Golden Eagles', position: 'Batsman', matches: 14, runs: 560, wickets: 1, catches: 5, strikeRate: 131.4, economy: 9.2, average: 40.0, recentForm: ['L', 'L', 'L', 'W', 'W'] },
  { id: 9, name: 'Meena Joshi', team: 'Red Wolves', position: 'All-Rounder', matches: 13, runs: 320, wickets: 11, catches: 9, strikeRate: 108.6, economy: 8.1, average: 26.7, recentForm: ['L', 'L', 'W', 'L', 'L'] },
  { id: 10, name: 'Vijay Das', team: 'Thunder Hawks', position: 'Bowler', matches: 14, runs: 62, wickets: 22, catches: 6, strikeRate: 72.3, economy: 7.2, average: 8.4, recentForm: ['W', 'W', 'L', 'W', 'W'] },
  { id: 11, name: 'Anita Chopra', team: 'Silver Foxes', position: 'Batsman', matches: 14, runs: 490, wickets: 0, catches: 4, strikeRate: 118.5, economy: 0, average: 35.0, recentForm: ['L', 'W', 'L', 'L', 'W'] },
  { id: 12, name: 'Rohit Gupta', team: 'Black Panthers', position: 'Bowler', matches: 13, runs: 55, wickets: 16, catches: 2, strikeRate: 68.9, economy: 8.9, average: 9.8, recentForm: ['L', 'L', 'W', 'L', 'L'] },
];
