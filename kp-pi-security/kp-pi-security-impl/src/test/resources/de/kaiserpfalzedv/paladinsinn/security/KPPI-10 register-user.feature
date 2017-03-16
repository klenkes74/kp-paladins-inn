Feature: I want to register an account.

  Scenario: I want to register a new account.
    * Given account 'A' does not exist.
    * When creating account 'A'.
    * Then account 'A' does exist.

  Scenario: I want to register an already taken account.
    * Given account 'A' does ist.
    * When creating account 'A'.
    * Then a failure is returned that account 'A' already exists.