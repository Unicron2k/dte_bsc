<?php
interface CatalogueInterface {
    public function showAllCatalogues() : array;                    // Return array with all catalogues
    public function showCatalogue(int $CatalogueID) : Catalogue;    // Return specific Catalogue
    public function addCatalogue(string $Name, int $Catalogue_CatalogueID);
    public function fetchLastCatalogueID(): int;                    // Return la
}