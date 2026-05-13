# AWS Systems Manager Configuration Hub with Change Management

This document provides a complete step-by-step procedure to set up **AWS Systems Manager** as a central configuration and change management hub for your AWS account.

---

## Phase 1: Prerequisites and Initial Setup

### Step 1: Verify Account Prerequisites
1. Sign in to the AWS Management Console with an account that has administrative permissions.
2. Open **IAM Console** and confirm your user/role has access to:
   - Systems Manager
   - IAM
   - EC2
   - SNS
   - CloudWatch
   - CloudTrail
   - AWS Config
3. Confirm required Systems Manager service-linked roles exist.
4. Verify billing is enabled for your account.
5. Confirm target regions are selected for deployment.

### Step 2: Install and Configure SSM Agent
1. Open **EC2 Console** and list your managed instances.
2. For each EC2 instance:
   - Verify SSM Agent is installed.
   - Verify agent service is running.
3. For on-premises servers:
   - Download SSM Agent from AWS documentation.
   - Install and start the service.
   - Register hybrid nodes in Systems Manager.
4. Create or validate an IAM instance profile with:
   - `AmazonSSMManagedInstanceCore` policy.
5. Attach the instance profile to all managed EC2 instances.
6. Validate managed node connectivity in **Systems Manager > Fleet Manager / Managed nodes**.

### Step 3: Set Up Systems Manager Quick Setup
1. Navigate to **Systems Manager > Quick Setup**.
2. Choose **Host Management**.
3. Select target accounts and regions.
4. Configure:
   - Update SSM Agent every 30 days
   - Collect inventory every 30 minutes
   - Scan instances for patch compliance daily
   - Install CloudWatch Agent
5. Deploy Quick Setup and verify successful association status.

---

## Phase 2: Configure Change Management Framework

### Step 4: Set Up Change Manager
1. Open **Systems Manager > Change Manager**.
2. Complete initial setup:
   - Select identity management model (IAM or IAM Identity Center).
   - Configure template approvers/reviewers.
   - Configure notification channels (SNS).
3. Validate permissions for requesters, approvers, and implementers.

### Step 5: Create SNS Topics for Notifications
1. Open **Amazon SNS Console**.
2. Create topics:
   - `change-manager-approvals`
   - `change-manager-notifications`
   - `patch-manager-results`
3. Add subscriptions (email/SMS/chat integrations) for stakeholders.
4. Confirm subscriptions.
5. Wire SNS topics to Change Manager and CloudWatch alarms.

### Step 6: Configure Change Calendar
1. Open **Systems Manager > Change Calendar**.
2. Create a calendar for maintenance and change governance.
3. Define blackout periods:
   - Holidays
   - Financial close windows
   - Business-critical events
4. Define allowed maintenance periods.
5. Publish calendar and validate integration with change templates.

---

## Phase 3: Set Up Patch Management

### Step 7: Configure Patch Manager
1. Open **Systems Manager > Patch Manager**.
2. Review AWS-managed default patch baselines per OS.
3. Create custom patch baselines (if needed):
   - Approval rules
   - Auto-approval delays
   - Explicitly rejected patches
4. Set baseline as default where appropriate.

### Step 8: Create Patch Groups
1. In **EC2 Console**, tag instances:
   - Key: `Patch Group`
   - Values: `Production`, `Development`, `Testing`
2. Verify tagging consistency across all target instances.
3. Register each patch group to the correct patch baseline.

### Step 9: Set Up Maintenance Windows
1. Open **Systems Manager > Maintenance Windows**.
2. Create windows per environment:
   - Production: weekend nights
   - Development: weekday evenings
   - Testing: daily off-hours
3. Register targets using patch groups.
4. Register patch tasks:
   - `AWS-RunPatchBaseline`
   - optional pre/post validation tasks
5. Configure concurrency, error threshold, and notification settings.

---

## Phase 4: Enable Monitoring and Compliance

### Step 10: Configure AWS Config
1. Open **AWS Config Console**.
2. Enable the configuration recorder in each target region.
3. Include Systems Manager compliance resource types:
   - `SSM:PatchCompliance`
   - `SSM:AssociationCompliance`
4. Configure delivery channel to S3 (and optionally SNS).
5. Verify compliance items are recorded.

### Step 11: Set Up CloudWatch Monitoring
1. Open **CloudWatch Console**.
2. Create alarms for:
   - Patch compliance failures
   - Change request failures
   - SSM Agent connectivity failures
3. Connect alarm actions to SNS topics.
4. Create CloudWatch dashboards for operational visibility.

### Step 12: Enable CloudTrail Logging
1. Open **CloudTrail Console**.
2. Ensure at least one trail captures management events.
3. Verify Systems Manager API calls are logged.
4. Configure log delivery to:
   - S3
   - CloudWatch Logs (recommended for alerting/search)
5. Confirm log retention and access controls.

---

## Phase 5: Create Change Templates and Validate

### Step 13: Create Change Templates
1. Return to **Change Manager**.
2. Create templates for:
   - Emergency patching
   - Routine maintenance
   - Configuration updates
3. Define:
   - Required fields
   - Risk level
   - Approval chains
   - Rollback plan references

### Step 14: Test the End-to-End Process
1. Submit a test change request from a template.
2. Validate approval workflow and notifications.
3. Execute a test patch operation in non-production.
4. Confirm maintenance window execution and task outcomes.
5. Validate compliance and audit evidence in Config/CloudTrail/Systems Manager.

### Step 15: Set Up Reporting and Operational Review
1. Configure **Systems Manager Compliance** views and filters.
2. Enable Resource Data Sync (optional but recommended for multi-account reporting).
3. Build recurring reports using Lambda/EventBridge (if required).
4. Publish CloudWatch dashboards for leadership and operations teams.
5. Define a monthly review cadence for exceptions and overdue remediation.

---

## Services and Components Summary

| Service/Component | Purpose | Configuration Status | Key Features |
|---|---|---|---|
| AWS Systems Manager | Central management hub | ✅ Core service | Unified operations console |
| Change Manager | Change approval workflow | 🔧 Requires setup | Pre-approved templates, approval workflows |
| Patch Manager | OS-level patch management | 🔧 Requires setup | Automated patching, compliance tracking |
| Quick Setup | Automated baseline configuration | ✅ Recommended first step | Multi-account/region setup |
| SSM Agent | Node communication | 🔧 Must install/verify | Enables remote management |
| Change Calendar | Maintenance scheduling | 🔧 Requires setup | Blackout periods, maintenance windows |
| Maintenance Windows | Scheduled operations | 🔧 Requires setup | Controlled change execution |
| AWS Config | Configuration tracking | 🔧 Requires setup | Change history, compliance monitoring |
| Amazon SNS | Notifications | 🔧 Requires setup | Email/SMS alerts for approvals and failures |
| CloudWatch | Monitoring and alarms | 🔧 Requires setup | Metrics, logs, automated responses |
| CloudTrail | Audit logging | ✅ Usually enabled | API call tracking |
| IAM Roles/Policies | Access control | 🔧 Requires setup | Service permissions, user access |
| Patch Baselines | Patch approval rules | 🔧 Requires customization | Define patch approval/install criteria |
| Patch Groups | Instance organization | 🔧 Requires tagging | Group instances by environment |
| Automation Runbooks | Standardized procedures | 🔧 Create custom ones | Automated task execution |
| Compliance Reporting | Status tracking | 🔧 Requires setup | Patch and configuration compliance |
| Resource Data Sync | Centralized reporting | 🔧 Optional setup | Aggregate data across accounts/regions |

---

## Recommended Post-Implementation Checklist

- [ ] All target nodes are visible as managed nodes in Systems Manager.
- [ ] Quick Setup associations are compliant.
- [ ] Change Manager templates and approval workflows are active.
- [ ] Patch groups and baselines are correctly mapped.
- [ ] Maintenance windows are scheduled and tested.
- [ ] CloudWatch alarms and SNS notifications are validated.
- [ ] CloudTrail and AWS Config capture required governance evidence.
