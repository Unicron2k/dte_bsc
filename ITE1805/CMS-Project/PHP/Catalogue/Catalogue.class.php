<?php
class Catalogue {
    private $CatalogueID;
    private $Name;
    private $Catalogue_CatalogueID;

    function __construct() {
    }

    // Getters
    function getCatalogueID() {
        return $this->CatalogueID;
    }
    function getName() {
        return $this->Name;
    }
    function getCatalogue_CatalogueID() {
        return $this->Catalogue_CatalogueID;
    }

    // Setters
    function setCatalogueID($CatalogueID) {
        $this->CatalogueID = $CatalogueID;
    }
    function setName($Name) {
        $this->Catalogue_CatalogueID = $Name;
    }
    function setCatalogue_CatalogueID($Catalogue_CatalogueID) {
        $this->Catalogue_CatalogueID = $Catalogue_CatalogueID;
    }

}