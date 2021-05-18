CREATE TABLE `oppslag` (
  `id` int(11) NOT NULL,
  `kategori` int(11) NOT NULL DEFAULT 0,
  `tittel` varchar(50) CHARACTER SET latin1 NOT NULL DEFAULT '',
  `ingress` text CHARACTER SET latin1 NOT NULL,
  `oppslagtekst` text CHARACTER SET latin1 NOT NULL,
  `bruker` varchar(12) CHARACTER SET latin1 NOT NULL DEFAULT '',
  `dato` date NOT NULL DEFAULT current_timestamp(),
  `treff` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `kategori` (
  `kat_id` int(4) NOT NULL,
  `navn` varchar(30) CHARACTER SET latin1 NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci PACK_KEYS=0;

INSERT INTO `kategori` (`kat_id`, `navn`)
VALUES (1, "Annonseringer"),
(2, "Forespørsler"),
(3, "Funnet");

INSERT INTO `oppslag` (`id`,`kategori`,`tittel`,`ingress`,`oppslagtekst`,`bruker`,`dato`,`treff`)
VALUES (1,1,"Test","Dette er en test av oppslagstavla","Her skal jeg legge inn eksempeldata", "Frank", current_timestamp(), 18),
(2,2,"Katt savnet!","Savner katten min, den er borte","Trenger hjelp til å finne katten min!", "Pernille", current_timestamp(), 4),
(3,3,"Katt funnet!","Villkatt funnet","Villkatt ble funnet låst inn i gymsalen. Eieren bes komme til rektors kontor", "Nils", current_timestamp(), 1),
(4,2,"Fest!","Årets party holdes hos meg!","Dersom noen er skikkelig gira på å bli drita, kom innom!", "Asgeir", current_timestamp(), 42);