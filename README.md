# InvoiceMatcher

## Overview
Invoice Matcher is a lightweight desktop tool built with Java Swing that compares two batches of invoices in CSV format from different systems.
It detects discrepancies between both datasets, showing which invoices exist in one file but not the other — simplifying financial reconciliation and commission tracking.

## Features
- Load CSV files directly from the default Downloads directory.
- Source-specific parsers for handling different CSV formats.
- Bidirectional comparison — shows invoices in A not in B, and vice versa.
- Simple and responsive UI built with Swing.
- Error handling via popup dialogs for parsing or I/O issues.
- Clear visualization of unmatched invoices in two scrollable panels.

## Tech Stack
- **Language:** Java 17+
- **GUI Framework:** Swing
- **Build tool:** Maven
- **CSV parsing:** OpenCSV
 
## Dependencies
| Dependency | Version | Purpose |
|-------------|----------|----------|
| Java | 17 | Main programming language |
| OpenCsv | 5.9 | CSV Parsing |

## Setup and Execution
1. Clone this repository  
   ```bash
   git clone https://github.com/Alvaro05RB/InvoiceMatcher
2. Run the project
Open the project in your IDE (e.g., IntelliJ IDEA or Eclipse) and run App.java from the src/presentation directory.
