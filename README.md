# Movie Information System

A console-based application that retrieves detailed movie information based on a film's title or actor using various data sources, including a MySQL database, DBpedia, OpenMovieDB, and web scraping.

## Features:
- **Retrieve Film Information**: Fetch movie details like release date, genre, plot, budget, and income by title.
- **Actor Filmography**: Get a list of films for a specific actor.
- **Web Scraping**: Scrapes movie data (distributors, genres) from "The Numbers" website, storing it in CSV format.

## Components:
- **Main Logic (Mediator Class)**: Manages user queries, fetching data from local DB and external APIs.
- **Database (MySQL)**: Stores and retrieves movie data.
- **DBpedia Client**: Fetches film-related information from DBpedia using SPARQL.
- **OpenMovieDB Client**: Retrieves movie data (plot, year) from the OpenMovieDB API.
- **Scraper**: Scrapes movie data from "The Numbers" website.

## Setup:
1. **Database**: Set up MySQL and create a `film` table with relevant fields.
2. **Dependencies**: Install required libraries (JDBC, Jena, Jsoup).
3. **API Keys**: Configure OpenMovieDB API key.

## Usage:
1. Run the application and select an option:
   - Fetch movie details by title.
   - Fetch films by actor.
2. The system will query the local DB first; if no data is found, it will call external sources for enrichment.

## Technologies Used:
- **Java**: Core programming language for implementing the application logic.
- **MySQL**: Relational database for storing and retrieving movie data.
- **JDBC**: Java API to interact with the MySQL database.
- **Jsoup**: Java library for scraping movie data from websites like "The Numbers".
- **Apache Jena**: Framework for querying DBpedia using SPARQL to retrieve movie-related information.
- **XPath**: Used for navigating and extracting data from XML documents (e.g., OpenMovieDB responses).
- **OpenMovieDB API**: Provides movie details such as title, plot, and release year.

## Future Improvements:
- Error handling, caching, multi-threading, and GUI integration.
