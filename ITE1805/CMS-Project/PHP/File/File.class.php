<?php
class file {
    private $FileID;
    private $File;
    private $UserID;
    private $Author; // if someone else created the file
    private $Filename;
    private $ServerFilename;
    private $Size;
    private $Mimetype;
    private $Description;
    private $Accessed;
    private $Views;
    private $Date;
    private $Access;
    private $User_UserID;
    private $CatalogueID;
    private $Catalogue_CatalogueID;

    function __construct() {
    }

    /**
     * @return mixed
     */
    public function getFileID()
    {
        return $this->FileID;
    }

    /**
     * @param mixed $FileID
     */
    public function setFileID($FileID): void
    {
        $this->FileID = $FileID;
    }

    /**
     * @return mixed
     */
    public function getFile()
    {
        return $this->File;
    }

    /**
     * @param mixed $File
     */
    public function setFile($File): void
    {
        $this->File = $File;
    }

    /**
     * @return mixed
     */
    public function getUserID()
    {
        return $this->UserID;
    }

    /**
     * @param mixed $UserID
     */
    public function setUserID($UserID): void
    {
        $this->UserID = $UserID;
    }

    /**
     * @return mixed
     */
    public function getAuthor()
    {
        return $this->Author;
    }

    /**
     * @param mixed $Author
     */
    public function setAuthor($Author): void
    {
        $this->Author = $Author;
    }

    /**
     * @return mixed
     */
    public function getFilename()
    {
        return $this->Filename;
    }

    /**
     * @param mixed $Filename
     */
    public function setFilename($Filename): void
    {
        $this->Filename = $Filename;
    }

    /**
     * @return mixed
     */
    public function getServerFilename()
    {
        return $this->ServerFilename;
    }

    /**
     * @param mixed $ServerFilename
     */
    public function setServerFilename($ServerFilename): void
    {
        $this->ServerFilename = $ServerFilename;
    }

    /**
     * @return mixed
     */
    public function getSize()
    {
        return $this->Size;
    }
    public function getSizeInMb()
    {
        return round(($this->Size) * pow(2, -20), 2) ;// n bytes =  n * 2^-20 Mb;
    }

    /**
     * @param mixed $Size
     */
    public function setSize($Size): void
    {
        $this->Size = $Size;
    }

    /**
     * @return mixed
     */
    public function getMimetype()
    {
        return $this->Mimetype;
    }

    /**
     * @param mixed $Mimetype
     */
    public function setMimetype($Mimetype): void
    {
        $this->Mimetype = $Mimetype;
    }

    /**
     * @return mixed
     */
    public function getDescription()
    {
        return $this->Description;
    }

    /**
     * @param mixed $Description
     */
    public function setDescription($Description): void
    {
        $this->Description = $Description;
    }

    /**
     * @return mixed
     */
    public function getAccessed()
    {
        return $this->Accessed;
    }

    /**
     * @param mixed $Accessed
     */
    public function setAccessed($Accessed): void
    {
        $this->Accessed = $Accessed;
    }

    /**
     * @return mixed
     */
    public function getViews()
    {
        return $this->Views;
    }

    /**
     * @param mixed $Views
     */
    public function setViews($Views): void
    {
        $this->Views = $Views;
    }

    /**
     * @return mixed
     */
    public function getDate()
    {
        return $this->Date;
    }

    /**
     * @param mixed $Date
     */
    public function setDate($Date): void
    {
        $this->Date = $Date;
    }

    /**
     * @return mixed
     */
    public function getAccess()
    {
        return $this->Access;
    }

    /**
     * @param mixed $Access
     */
    public function setAccess($Access): void
    {
        $this->Access = $Access;
    }

    /**
     * @return mixed
     */
    public function getUserUserID()
    {
        return $this->User_UserID;
    }

    /**
     * @param mixed $User_UserID
     */
    public function setUserUserID($User_UserID): void
    {
        $this->User_UserID = $User_UserID;
    }

    /**
     * @return mixed
     */
    public function getCatalogueID()
    {
        return $this->CatalogueID;
    }

    /**
     * @param mixed $CatalogueID
     */
    public function setCatalogueID($CatalogueID): void
    {
        $this->CatalogueID = $CatalogueID;
    }

    /**
     * @return mixed
     */
    public function getCatalogueCatalogueID()
    {
        return $this->Catalogue_CatalogueID;
    }

    /**
     * @param mixed $Catalogue_CatalogueID
     */
    public function setCatalogueCatalogueID($Catalogue_CatalogueID): void
    {
        $this->Catalogue_CatalogueID = $Catalogue_CatalogueID;
    }


}
?>