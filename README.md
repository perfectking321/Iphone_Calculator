# CalculatorApp

A small desktop calculator built with Java Swing.

## Requirements
- Java 8+ installed (JDK or JRE)

## Build & Run (Windows PowerShell)
1. Compile:

   javac "CalculatorApp.java"

2. Run:

   java CalculatorApp

The application opens a GUI window. It supports keyboard input (digits, +, -, *, /, Enter for equals, Esc/Delete to clear) and mouse clicks.

## Features
- Basic arithmetic with operator precedence
- Decimal support
- Backspace (keyboard) removes last character
- Clear (C button or Esc/Delete)

## Notes
- Results are formatted: integers are shown without decimals, otherwise rounded to two decimal places.
- If an error occurs during evaluation the display shows `Error`.

## License
Use as you like.