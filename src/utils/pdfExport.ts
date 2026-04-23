import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import type { Player, Team, Recommendation } from '../types';

export function exportToPDF(players: Player[], teams: Team[], recommendations: Recommendation[]): void {
  const doc = new jsPDF({ orientation: 'portrait', unit: 'mm', format: 'a4' });
  const pageWidth = doc.internal.pageSize.getWidth();
  const primaryColor: [number, number, number] = [26, 86, 219];
  const headerBg: [number, number, number] = [26, 86, 219];

  // ── Cover / Title ────────────────────────────────────────────────
  doc.setFillColor(...primaryColor);
  doc.rect(0, 0, pageWidth, 50, 'F');

  doc.setTextColor(255, 255, 255);
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(24);
  doc.text('Sports APL', pageWidth / 2, 22, { align: 'center' });

  doc.setFontSize(14);
  doc.setFont('helvetica', 'normal');
  doc.text('Performance Analytics Report', pageWidth / 2, 32, { align: 'center' });

  doc.setFontSize(10);
  doc.text(`Generated: ${new Date().toLocaleDateString('en-IN', { day: '2-digit', month: 'long', year: 'numeric' })}`, pageWidth / 2, 42, { align: 'center' });

  // ── Summary Cards ────────────────────────────────────────────────
  doc.setTextColor(30, 41, 59);
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(13);
  doc.text('Season Summary', 14, 62);

  const topTeam = [...teams].sort((a, b) => b.points - a.points)[0];
  const topBatsman = [...players].sort((a, b) => b.runs - a.runs)[0];
  const topBowler = [...players].filter((p) => p.wickets > 0).sort((a, b) => b.wickets - a.wickets)[0];
  const totalMatches = teams.reduce((s, t) => s + t.wins + t.losses + t.draws, 0) / 2;

  const summaryItems = [
    { label: 'Total Matches', value: String(Math.round(totalMatches)) },
    { label: 'Teams', value: String(teams.length) },
    { label: 'Top Team', value: topTeam.name },
    { label: 'Leading Scorer', value: `${topBatsman.name} (${topBatsman.runs} runs)` },
    { label: 'Leading Wicket-Taker', value: `${topBowler.name} (${topBowler.wickets} wkts)` },
    { label: 'Registered Players', value: String(players.length) },
  ];

  const colW = (pageWidth - 28) / 3;
  summaryItems.forEach((item, i) => {
    const col = i % 3;
    const row = Math.floor(i / 3);
    const x = 14 + col * colW;
    const y = 68 + row * 22;

    doc.setFillColor(240, 245, 255);
    doc.roundedRect(x, y, colW - 4, 18, 2, 2, 'F');

    doc.setFont('helvetica', 'normal');
    doc.setFontSize(8);
    doc.setTextColor(100, 116, 139);
    doc.text(item.label, x + 4, y + 6);

    doc.setFont('helvetica', 'bold');
    doc.setFontSize(9);
    doc.setTextColor(30, 41, 59);
    doc.text(item.value, x + 4, y + 13);
  });

  // ── Points Table ─────────────────────────────────────────────────
  doc.setTextColor(30, 41, 59);
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(13);
  doc.text('Points Table', 14, 118);

  autoTable(doc, {
    startY: 122,
    head: [['#', 'Team', 'W', 'L', 'D', 'Pts', 'NRR']],
    body: [...teams]
      .sort((a, b) => b.points - a.points)
      .map((t, idx) => [
        idx + 1,
        t.name,
        t.wins,
        t.losses,
        t.draws,
        t.points,
        t.netRunRate >= 0 ? `+${t.netRunRate.toFixed(2)}` : t.netRunRate.toFixed(2),
      ]),
    styles: { fontSize: 9, cellPadding: 3 },
    headStyles: { fillColor: headerBg, textColor: [255, 255, 255], fontStyle: 'bold' },
    alternateRowStyles: { fillColor: [248, 250, 255] },
    columnStyles: { 0: { cellWidth: 10 }, 1: { cellWidth: 52 } },
  });

  // ── Player Statistics ─────────────────────────────────────────────
  doc.addPage();

  doc.setFillColor(...primaryColor);
  doc.rect(0, 0, pageWidth, 18, 'F');
  doc.setTextColor(255, 255, 255);
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(13);
  doc.text('Player Statistics', pageWidth / 2, 12, { align: 'center' });

  autoTable(doc, {
    startY: 24,
    head: [['Player', 'Team', 'Pos', 'M', 'Runs', 'Avg', 'SR', 'Wkts', 'Eco']],
    body: [...players]
      .sort((a, b) => b.runs - a.runs)
      .map((p) => [
        p.name,
        p.team,
        p.position,
        p.matches,
        p.runs,
        p.average.toFixed(1),
        p.strikeRate.toFixed(1),
        p.wickets,
        p.economy > 0 ? p.economy.toFixed(1) : '-',
      ]),
    styles: { fontSize: 8.5, cellPadding: 2.5 },
    headStyles: { fillColor: headerBg, textColor: [255, 255, 255], fontStyle: 'bold' },
    alternateRowStyles: { fillColor: [248, 250, 255] },
    columnStyles: {
      0: { cellWidth: 36 },
      1: { cellWidth: 34 },
      2: { cellWidth: 24 },
    },
  });

  // ── Recommendations ───────────────────────────────────────────────
  doc.addPage();

  doc.setFillColor(...primaryColor);
  doc.rect(0, 0, pageWidth, 18, 'F');
  doc.setTextColor(255, 255, 255);
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(13);
  doc.text('Recommendations', pageWidth / 2, 12, { align: 'center' });

  const priorityColors: Record<string, { bg: [number, number, number]; text: [number, number, number] }> = {
    high:   { bg: [254, 226, 226], text: [185, 28,  28]  },
    medium: { bg: [254, 243, 199], text: [146, 64,  14]  },
    low:    { bg: [220, 252, 231], text: [21,  128, 61]  },
  };

  let yPos = 26;
  const margin = 14;
  const cardW = pageWidth - margin * 2;

  recommendations.forEach((rec) => {
    if (yPos > 265) {
      doc.addPage();
      yPos = 20;
    }

    const pc = priorityColors[rec.priority];

    // Card background
    doc.setFillColor(248, 250, 252);
    doc.roundedRect(margin, yPos, cardW, 32, 2, 2, 'F');

    // Priority badge
    doc.setFillColor(...pc.bg);
    doc.roundedRect(margin + 2, yPos + 2, 22, 7, 1.5, 1.5, 'F');
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(7);
    doc.setTextColor(...pc.text);
    doc.text(rec.priority.toUpperCase(), margin + 13, yPos + 6.8, { align: 'center' });

    // Type badge
    doc.setFillColor(219, 234, 254);
    doc.roundedRect(margin + 26, yPos + 2, 20, 7, 1.5, 1.5, 'F');
    doc.setTextColor(29, 78, 216);
    doc.text(rec.type.toUpperCase(), margin + 36, yPos + 6.8, { align: 'center' });

    // Title
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(10);
    doc.setTextColor(15, 23, 42);
    doc.text(rec.title, margin + 4, yPos + 15);

    // Target
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(8.5);
    doc.setTextColor(71, 85, 105);
    doc.text(`Target: ${rec.targetName}`, margin + 4, yPos + 21);

    // Description
    const descLines = doc.splitTextToSize(rec.description, cardW - 8);
    doc.setFontSize(8);
    doc.setTextColor(100, 116, 139);
    doc.text(descLines[0], margin + 4, yPos + 27);

    // Metric on right
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(8);
    doc.setTextColor(71, 85, 105);
    const metricText = `${rec.metric}: ${rec.currentValue} → ${rec.targetValue}`;
    doc.text(metricText, pageWidth - margin - 4, yPos + 15, { align: 'right' });

    yPos += 36;
  });

  // ── Footer on every page ──────────────────────────────────────────
  const pageCount = doc.getNumberOfPages();
  for (let i = 1; i <= pageCount; i++) {
    doc.setPage(i);
    doc.setFillColor(241, 245, 249);
    doc.rect(0, 284, pageWidth, 13, 'F');
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(8);
    doc.setTextColor(148, 163, 184);
    doc.text('Sports APL — Performance Analytics', margin, 292);
    doc.text(`Page ${i} of ${pageCount}`, pageWidth - margin, 292, { align: 'right' });
  }

  doc.save(`SportsAPL_Report_${new Date().toISOString().split('T')[0]}.pdf`);
}
