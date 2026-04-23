# Security Tools Library

A curated collection of open-source security tools and resources.

---

## Web Application Scanning & Crawling

| Repository | Description |
|---|---|
| [OWASP ZAP](https://github.com/OWASP/ZAP) | Zed Attack Proxy – integrated web application security testing tool |
| [Nikto](https://github.com/nikto/nikto) | Web server scanner |
| [Nikto (sullo)](https://github.com/sullo/nikto) | Web server scanner (canonical source) |
| [Arachni](https://github.com/Arachni/arachni) | Web application security scanner framework |
| [w3af](https://github.com/andresriancho/w3af) | Web application attack and audit framework |
| [Wapiti](https://github.com/EnableSecurity/wapiti) | Web application vulnerability scanner |
| [OWASP Nettacker](https://github.com/OWASP/Nettacker) | Automated penetration testing framework |
| [OpenVAS Scanner](https://github.com/Greenbone/openvas-scanner) | Open vulnerability assessment scanner |

---

## SQL Injection

| Repository | Description |
|---|---|
| [sqlmap](https://github.com/sqlmapproject/sqlmap) | Automatic SQL injection and database takeover tool |

---

## Subdomain Enumeration & Reconnaissance

| Repository | Description |
|---|---|
| [Subfinder](https://github.com/projectdiscovery/subfinder) | Subdomain discovery tool |
| [OWASP Amass](https://github.com/OWASP/Amass) | In-depth attack surface mapping and asset discovery |
| [Sublist3r](https://github.com/aboul3la/Sublist3r) | Fast subdomains enumeration tool |
| [theHarvester](https://github.com/laramies/theHarvester) | OSINT email, subdomain, and name harvesting tool |

---

## Network Scanning & Port Discovery

| Repository | Description |
|---|---|
| [Nmap](https://github.com/nmap/nmap) | Network exploration and security auditing tool |
| [Naabu](https://github.com/projectdiscovery/naabu) | Fast port scanner |

---

## HTTP Probing & Web Fuzzing

| Repository | Description |
|---|---|
| [httpx](https://github.com/projectdiscovery/httpx) | Fast and multi-purpose HTTP toolkit |
| [ffuf](https://github.com/ffuf/ffuf) | Fast web fuzzer written in Go |
| [dirsearch](https://github.com/maurosoria/dirsearch) | Web path scanner |
| [gobuster](https://github.com/OJ/gobuster) | Directory/file, DNS, and VHost buster |

---

## Vulnerability Scanning & Automation

| Repository | Description |
|---|---|
| [Nuclei](https://github.com/projectdiscovery/nuclei) | Fast and customizable vulnerability scanner |
| [Katana](https://github.com/projectdiscovery/katana) | Next-generation crawling and spidering framework |
| [Interactsh](https://github.com/projectdiscovery/interactsh) | Out-of-band interaction gathering server and client |

---

## XSS & Injection

| Repository | Description |
|---|---|
| [XSStrike](https://github.com/s0md3v/XSStrike) | Advanced XSS detection suite |

---

## WAF Detection

| Repository | Description |
|---|---|
| [wafw00f](https://github.com/EnableSecurity/wafw00f) | Web Application Firewall detection and fingerprinting tool |

---

## JWT Tools

| Repository | Description |
|---|---|
| [jwt_tool](https://github.com/ticarpi/jwt_tool) | Toolkit for testing, tweaking, and cracking JWTs |

---

## Parameter Discovery

| Repository | Description |
|---|---|
| [Param Miner](https://github.com/PortSwigger/param-miner) | Burp Suite extension to identify hidden, unlinked parameters |

---

## Static Analysis & SAST

| Repository | Description |
|---|---|
| [Semgrep](https://github.com/semgrep/semgrep) | Lightweight static analysis for many languages |
| [Semgrep Rules](https://github.com/returntocorp/semgrep-rules) | Official and community Semgrep rules |
| [CodeQL](https://github.com/github/codeql) | Semantic code analysis engine by GitHub |
| [SonarQube](https://github.com/SonarSource/sonarqube) | Continuous code quality and security platform |
| [gosec](https://github.com/securego/gosec) | Go security checker |

---

## Secret Detection

| Repository | Description |
|---|---|
| [Gitleaks](https://github.com/gitleaks/gitleaks) | Detect hardcoded secrets in git repositories |
| [TruffleHog](https://github.com/trufflesecurity/trufflehog) | Searches through git repositories for secrets |
| [detect-secrets](https://github.com/Yelp/detect-secrets) | Enterprise-friendly way to detect secrets in code |

---

## Dependency & Container Vulnerability Scanning

| Repository | Description |
|---|---|
| [Grype](https://github.com/anchore/grype) | Vulnerability scanner for container images and filesystems |
| [Trivy](https://github.com/aquasecurity/trivy) | Comprehensive security scanner for containers and other artifacts |
| [dep-scan](https://github.com/owasp-dep-scan/dep-scan) | OWASP dependency audit and risk assessment tool |
| [OWASP Dependency-Check](https://github.com/jeremylong/DependencyCheck) | Software component analysis tool |
| [retire.js](https://github.com/retirejs/retire.js) | Scanner detecting use of JavaScript libraries with known vulnerabilities |

---

## Infrastructure as Code (IaC) Security

| Repository | Description |
|---|---|
| [KICS](https://github.com/Checkmarx/kics) | Finding security vulnerabilities in IaC |
| [Checkov](https://github.com/bridgecrewio/checkov) | Static analysis for IaC |
| [kube-bench](https://github.com/aquasecurity/kube-bench) | Checks Kubernetes against CIS Kubernetes Benchmark |
| [kube-hunter](https://github.com/aquasecurity/kube-hunter) | Hunts for security weaknesses in Kubernetes clusters |

---

## Runtime Security & Cloud

| Repository | Description |
|---|---|
| [Falco](https://github.com/falcosecurity/falco) | Cloud-native runtime security |
| [Cartography](https://github.com/lyft/cartography) | Consolidates infrastructure assets and relationships into a graph |
| [Prowler](https://github.com/ProwlerCloud/prowler) | AWS security tool for audits, hardening, and incident response |
| [InSpec](https://github.com/inspec/inspec) | Auditing and testing framework for infrastructure |
| [Ansible Lockdown](https://github.com/ansible/ansible-lockdown) | Ansible roles for security hardening |

---

## SBOM & Software Supply Chain

| Repository | Description |
|---|---|
| [Syft](https://github.com/anchore/syft) | CLI tool for generating an SBOM from container images and filesystems |
| [Microsoft SBOM Tool](https://github.com/microsoft/sbom-tool) | Scalable and enterprise-ready SBOM tool |
| [CycloneDX CLI](https://github.com/CycloneDX/cyclonedx-cli) | Cross-platform tool for SBOM analysis and manipulation |

---

## Offensive Security / Red Team / Post-Exploitation

| Repository | Description |
|---|---|
| [Metasploit Framework](https://github.com/rapid7/metasploit-framework) | Penetration testing framework |
| [Sliver](https://github.com/BishopFox/sliver) | Adversary simulation framework |
| [CrackMapExec](https://github.com/byt3bl33d3r/CrackMapExec) | Post-exploitation tool for Active Directory environments |
| [Impacket](https://github.com/fortra/impacket) | Collection of Python classes for network protocols |
| [Responder](https://github.com/lgandx/Responder) | LLMNR, NBT-NS, and MDNS poisoner |
| [PowerSploit](https://github.com/PowerShellMafia/PowerSploit) | PowerShell post-exploitation framework |
| [BloodHound](https://github.com/SpecterOps/BloodHound) | Active Directory attack path analysis tool |

---

## Payloads & Cheat Sheets

| Repository | Description |
|---|---|
| [PayloadsAllTheThings](https://github.com/swisskyrepo/PayloadsAllTheThings) | A list of useful payloads and bypasses for web application security |
| [OWASP CheatSheet Series](https://github.com/OWASP/CheatSheetSeries) | Concise, high-value security guidance for developers |

---

## Security Standards & Guides

| Repository | Description |
|---|---|
| [OWASP WSTG](https://github.com/OWASP/wstg) | Web Security Testing Guide – comprehensive web security testing methodology |
| [OWASP ASVS](https://github.com/OWASP/ASVS) | Application Security Verification Standard |
| [OWASP MASTG](https://github.com/OWASP/mastg) | Mobile Application Security Testing Guide |

---

## Awesome Lists & References

| Repository | Description |
|---|---|
| [Static Analysis Tools](https://github.com/analysis-tools-dev/static-analysis) | Curated list of static analysis tools |
| [Awesome Static Analysis](https://github.com/awesome-security/awesome-static-analysis) | Another curated list of static analysis tools |
| [Awesome Pentest](https://github.com/enaqx/awesome-pentest) | Collection of penetration testing resources |
| [Awesome Hacking](https://github.com/Hack-with-Github/Awesome-Hacking) | List of awesome hacking tools and resources |
| [RedTeam Tactics and Techniques](https://github.com/RedTeamOperations/RedTeam-Tactics-and-Techniques) | Red team notes, techniques, and procedures |
