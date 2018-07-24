# Android ND Capstone Project (NFL Crimewatch)

## Project Overview
This project is Doug Brown's capstone for Udacity's Android Nano-Degree program. It has the following features:
* Utilizes the data api available at nflarrest.com to source arrests of NFL players since 2000
* Has activity / fragments for displaying the most recent arrest data by team, player position, or crime
* Has activity / fragments for displaying a list of all arrests in the selected category
* Has activity / fragments for displaying a list of all arrests for a selected player
* Has a preferences activity for selecting favorite team
* Favorite team is auto-populated on initial startup if geo-location position is available
* Provides a widget for displaying the most recent arrest for the favorite team
* All data is managed through Room and a combination of Runnables and View Models
* All static strings and values are managed through resource files
* Layouts for mobile (portrait and landscape) and tablet
* Google Ads are served for free version. Paid version has no ad.

## To Review

* Clone this source code
* The crimewatch keystore has a password of immapassword
* Target SDK is 27, minimum SDK is 21