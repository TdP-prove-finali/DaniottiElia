-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Feb 23, 2020 alle 01:19
-- Versione del server: 10.4.6-MariaDB
-- Versione PHP: 7.3.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `safari`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `mappa`
--

CREATE TABLE `mappa` (
  `idTratta` varchar(2) NOT NULL,
  `inizio` char(1) NOT NULL,
  `fine` char(1) NOT NULL,
  `lunghezza` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `mappa`
--

INSERT INTO `mappa` (`idTratta`, `inizio`, `fine`, `lunghezza`) VALUES
('AB', 'A', 'B', 13),
('AC', 'A', 'C', 17),
('AZ', 'A', 'Z', 15),
('BC', 'B', 'C', 7),
('BD', 'B', 'D', 8),
('CD', 'C', 'D', 22),
('DE', 'D', 'E', 22),
('DN', 'D', 'N', 19),
('DP', 'D', 'P', 11),
('EF', 'E', 'F', 21),
('EG', 'E', 'G', 18),
('EL', 'E', 'L', 22),
('EN', 'E', 'N', 28),
('FG', 'F', 'G', 5),
('FH', 'F', 'H', 32),
('GH', 'G', 'H', 4),
('HI', 'H', 'I', 5),
('IL', 'I', 'L', 5),
('IM', 'I', 'M', 20),
('LM', 'L', 'M', 16),
('MQ', 'M', 'Q', 22),
('NO', 'N', 'O', 7),
('NS', 'N', 'S', 12),
('OP', 'O', 'P', 6),
('OZ', 'O', 'Z', 14),
('PZ', 'P', 'Z', 4),
('QR', 'Q', 'R', 5),
('QT', 'Q', 'T', 24),
('RS', 'R', 'S', 9),
('RT', 'R', 'T', 14),
('ST', 'S', 'T', 7);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `mappa`
--
ALTER TABLE `mappa`
  ADD PRIMARY KEY (`idTratta`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
