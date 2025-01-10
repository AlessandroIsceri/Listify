-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Gen 09, 2025 alle 16:04
-- Versione del server: 10.4.32-MariaDB
-- Versione PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `listify`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `activity`
--

CREATE TABLE `activity` (
  `id` int(11) NOT NULL,
  `name` char(50) NOT NULL,
  `priority` int(11) NOT NULL,
  `expirationDate` date DEFAULT NULL,
  `category` char(50) NOT NULL,
  `list_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `activity`
--

INSERT INTO `activity` (`id`, `name`, `priority`, `expirationDate`, `category`, `list_id`) VALUES
(255, 'Scrivere Relazione', 1, '2025-02-10', 'To Do', 213),
(256, 'Preparare la Presentazione Finale', 2, '2025-02-12', 'To Do', 213),
(257, 'Correzione Relazione', 2, '2025-02-18', 'To Do', 213),
(258, 'Creare Repo GitLab', 7, '2024-12-25', 'Completed', 213),
(259, 'Scrivere i Casi di Test', 6, '2025-01-09', 'In Progress', 213),
(260, 'Implementare il Controller', 7, '2025-01-22', 'In Progress', 213),
(261, 'Imparare una Nuova Ricetta', 5, '2025-01-15', 'To Do', 214),
(262, 'Allenarsi 3 Volte a Settimana', 3, NULL, 'In Progress', 214),
(263, 'Leggere 2 libri', -1, NULL, 'Completed', 214),
(264, 'Corso Online sulla Cucina', 7, '2025-04-24', 'Completed', 214),
(265, 'Fare la Valigia', 1, '2025-02-28', 'To Do', 215),
(266, 'Scegliere Città da Visitare', 4, '2025-01-22', 'Completed', 215),
(267, 'Scegliere Hotel', 2, '2025-01-15', 'Completed', 215),
(268, 'Prenotare Volo', 3, '2025-01-15', 'In Progress', 215),
(269, 'Implementare Business Logic', 6, '2025-01-17', 'Completed', 213),
(270, 'Implementare Classi di Dominio', 2, '2025-01-05', 'Completed', 213);

-- --------------------------------------------------------

--
-- Struttura della tabella `todolist`
--

CREATE TABLE `todolist` (
  `id` int(11) NOT NULL,
  `name` char(50) NOT NULL,
  `username` char(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `todolist`
--

INSERT INTO `todolist` (`id`, `name`, `username`) VALUES
(213, 'Progetto PSS', 'aless'),
(214, 'Obiettivi Personali', 'aless'),
(215, 'Viaggio Spagna', 'aless'),
(216, 'Regali Natale 2024', 'aless'),
(217, 'Serie TV/Film da Guardare', 'aless'),
(218, 'Lista della Spesa', 'aless'),
(219, 'Progetto ML', 'aless'),
(220, 'Acquisti per la Casa', 'aless'),
(221, 'Progetti Fai-Da-Te', 'aless'),
(222, 'Organizzazione Compleanno', 'aless');

-- --------------------------------------------------------

--
-- Struttura della tabella `user`
--

CREATE TABLE `user` (
  `email` char(50) NOT NULL,
  `password` char(50) NOT NULL,
  `username` char(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `user`
--

INSERT INTO `user` (`email`, `password`, `username`) VALUES
('aless@gmail.com', 'pw', 'aless'),
('alex@gmail.com', 'pw', 'alex');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `activity`
--
ALTER TABLE `activity`
  ADD PRIMARY KEY (`id`),
  ADD KEY `todolist_id_constraint` (`list_id`);

--
-- Indici per le tabelle `todolist`
--
ALTER TABLE `todolist`
  ADD PRIMARY KEY (`id`),
  ADD KEY `username_constraint` (`username`);

--
-- Indici per le tabelle `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `activity`
--
ALTER TABLE `activity`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=280;

--
-- AUTO_INCREMENT per la tabella `todolist`
--
ALTER TABLE `todolist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=232;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `activity`
--
ALTER TABLE `activity`
  ADD CONSTRAINT `todolist_id_constraint` FOREIGN KEY (`list_id`) REFERENCES `todolist` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `todolist`
--
ALTER TABLE `todolist`
  ADD CONSTRAINT `username_constraint` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
