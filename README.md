Banksys
=======

A multitier banking application.

A graded integration project in the context of CAS Distributed Systems.

Features
--------

The project consists of the folowing partis/applicatiosn:

  * a backend server tier managing accounts, customers and transactions
  * a simulated ATM application (CLI) talking to the backend with RMI
  * a simulated counter desk app (CLI) talking to the backend with RMI
  * a JSP-based ebanking application talking to the backend with RMI

There was a interbanking clearing server provided by the lectureres to enable transaction between different banks. Therefore the backend server provides a webservice (JAX-WS) to receive transactions and implements the webservice of the clearing server to send transactions to other banks.

Credits
-------

This project was a group work. Therefore credits go to the following students:

  * Lukas Feuz
  * Lukas Kamber
  * Michael Rolli

- - -
This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Switzerland License.

