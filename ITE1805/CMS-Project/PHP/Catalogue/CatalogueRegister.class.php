<?php
class CatalogueRegister implements CatalogueInterface {
    private $db;

    public function __construct(PDO $db) {
        $this->db = $db;
    }

    public function showAllCatalogues(): array
    {
        $catalogues = array();
        try {
            $stmt = $this->db->query("SELECT * FROM Catalogue ORDER BY Name");
            $stmt->execute();
            while ($catalogue = $stmt->fetchObject("Catalogue")) {
                $catalogues[] = $catalogue;
            }
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return $catalogues;
    }

    public function showCatalogue(int $CatalogueID): Catalogue
    {
        try {
            $stmt = $this->db->prepare("SELECT * FROM Catalogue WHERE CatalogueID = :catalogueID");
            $stmt->bindParam(":catalogueID", $CatalogueID, PDO::PARAM_INT);
            $stmt->execute();

            return $stmt->fetchObject("Catalogue");
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function addCatalogue(string $Name, int $Catalogue_CatalogueID)
    {
        if ($Catalogue_CatalogueID != 0) {
            // Add sub Catalogue
            try {
                $stmt = $this->db->prepare("INSERT INTO `Catalogue`(`Name`,`Catalogue_CatalogueID`) VALUES (:name, :catalogue_CatalogueID)");
                $stmt->bindParam(':name', $Name);
                $stmt->bindParam(':catalogue_CatalogueID', $Catalogue_CatalogueID);

                $result = $stmt->execute();
                if ($result) {
                    alert("Katalog lastet opp!");
                    return true;
                } else {
                    alert("Katalog ble ikke lastet opp!");
                    return false;
                }
            } catch (InvalidArgumentException $e) {
                print $e->getMessage() . "Failed adding catalogue!" . PHP_EOL;
            }
        } else {
            // Add master Catalogue
            try {
                $stmt = $this->db->prepare("INSERT INTO `Catalogue`(`Name`) VALUES (:name)");
                $stmt->bindParam(':name', $Name);

                $result = $stmt->execute();
                if ($result)
                    alert("Katalog lastet opp!");
                else
                    alert("Katalog ble ikke lastet opp!");

            } catch (InvalidArgumentException $e) {
                print $e->getMessage() . "Failed adding catalogue!" . PHP_EOL;
            }
        }
    }

    public function fetchLastCatalogueID(): int
    {
        // TODO: Implement fetchLastCatalogueID() method.

    }
}