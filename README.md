# PatientPortal
Automated end-to-end testing of a dynamic web application involving scenario-driven workflows, conditional page redirections, and email-based OTP validation. The automation suite was built using Selenium WebDriver (Java) with robust handling of dynamic dropdowns, scrolling, element visibility, and Excel-based data input/output.Implemented logic to run selective scenarios based on status flags (PENDING, DONE), ensuring reusability and efficiency across multiple test runs.

This project automates a dynamic web application that includes conditional navigation, email-based OTP validation, and Excel-driven scenario execution using Selenium WebDriver in Java.

🚀 Features
✅ Scenario-based execution using Excel (PENDING, DONE, etc.)

✅ Conditional redirection logic (e.g., GMG / Ultomiris to custom pages)

✅ Email OTP handling with wait mechanism

✅ Scroll & interact with dynamic dropdowns and hidden elements

✅ Robust error handling with retry logic

✅ Status updates back to Excel after execution

🛠️ Tech Stack
Language: Java

Automation: Selenium WebDriver

Data Handling: Apache POI (Excel)

Browser: ChromeDriver

📌 How to Run
Clone the repo

Update your Excel scenarios (mark the ones you want as PENDING)

Run the Runner.java file

Output Excel will update each scenario's status as DONE or PENDING

💡 Tip
To run a specific scenario, just mark it as PENDING in Excel and the script will pick it up. Already completed scenarios are ignored to save time.

