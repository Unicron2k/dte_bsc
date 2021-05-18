<?php
class FileRegister implements FileInterface {

	private $db;

	public function __construct(PDO $db) {
		$this->db = $db;
	}

    public function showAllFiles(int $access): array
    {
        $files = array();

        if ($access == 0) {
            try {
                $stmt = $this->db->query("SELECT * FROM File WHERE Access=0 ORDER BY Date");
                $stmt->execute();
                while ($file = $stmt->fetchObject("File")) {
                    $files[] = $file;
                }
            } catch (Exception $e) {
                print $e->getMessage() . PHP_EOL;
            }
            return $files;
        } else if ($access == 1) {
            try {
                $stmt = $this->db->query("SELECT * FROM File ORDER BY Date");
                $stmt->execute();
                while ($file = $stmt->fetchObject("File")) {
                    $files[] = $file;
                }
            } catch (Exception $e) {
                print $e->getMessage() . PHP_EOL;
            }
            return $files;
        }
    }

    public function showFile(int $id): File
    {
        // Return given file with FileID
        try {
            $stmt = $this->db->prepare("SELECT * FROM File WHERE FileID = :id");
            $stmt->bindParam(":id", $id, PDO::PARAM_INT);
            $stmt->execute();
            return $stmt->fetchObject("File");
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function addFile(File $file, int $UserID)
    {
        // Add file to table `File`
        try {
            $stmt = $this->db->prepare("INSERT INTO `File`(`File`, `UserID`, `Author`, `Filename`, `ServerFilename`, `Size`, `Mimetype`, `Description`, `Accessed`, `Views`, `Date`            ,`Access` , `User_UserID`, `CatalogueID`, `Catalogue_CatalogueID`)                                                            
                                                            VALUES (:file,   :userID,  :author,  :filename,  :serverFilename,  :size,  :mimetype,  :description ,  0        ,   0    , :opprettet        ,:access  , :user_UserID , :catalogueID,:Catalogue_CatalogueID)");
            $opprettet = date("Y-m-d H:i:s");
            $stmt->bindValue(':file', $file->getFile(), PDO::PARAM_LOB); // TODO: LOB??
            $stmt->bindValue(':userID', $file->getUserID(), PDO::PARAM_INT);
            $stmt->bindValue(':author', $file->getAuthor(), PDO::PARAM_STR); // TODO: Make sure author is passed to obj $file
            $stmt->bindValue(':filename', $file->getFileName(), PDO::PARAM_STR);
            $stmt->bindValue(':serverFilename', $file->getFileName(), PDO::PARAM_STR); // TODO: Why do we need ServerFilename?
            $stmt->bindValue(':size', $file->getSize(), PDO::PARAM_INT);
            $stmt->bindValue(':mimetype', $file->getMimetype(), PDO::PARAM_STR);
            $stmt->bindParam(':opprettet', $opprettet);
            $stmt->bindValue(':description', $file->getDescription(), PDO::PARAM_STR);
            $stmt->bindValue(':access', $file->getAccess(), PDO::PARAM_INT);
            $stmt->bindValue(':user_UserID', $file->getUserUserID(), PDO::PARAM_INT);
            $stmt->bindValue(':catalogueID', $file->getCatalogueID(), PDO::PARAM_INT);
            $stmt->bindValue(':Catalogue_CatalogueID', $file->getCatalogueCatalogueID(), PDO::PARAM_INT);
            $result = $stmt->execute();
            if ($result) {
                alert("Fil lastet opp!");
                return true;
            } else {
                alert("Fil ble ikke lastet opp!");
                return false;
            }
            function alert($msg) {
                echo "<script type='text/javascript'>alert('$msg');</script>";
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function updateFile(File $file, int $id): bool
    {
        try {
            $stmt = $this->db->prepare("UPDATE File SET Description= :description WHERE FileID= :fileID");
            $stmt->bindParam(':fileID', $id, PDO::PARAM_INT);
            $stmt->bindValue(':description', $file->hentDescription(), PDO::PARAM_STR);
            $result = $stmt->execute();

            if ($result) {
                return true;
            } else {
                echo "Feil ved oppdatering av fil!";
                return false;
            }
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function deleteFile(int $fileID): bool
    {
        try {
            $stmt = $this->db->prepare("DELETE FROM File Where FileID= :fileID");
            $stmt->bindParam(':fileID', $fileID, PDO::PARAM_INT);
            $result = $stmt->execute();

            if ($result) {
                return true;
            } else {
                echo "Feil ved sletting av fil!";
                return false;
            }
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function showAllFilesInCatalogue(int $catalogueID): Array
    {
        // Return array of files in given Catalogue
        $files = array();
        try {
            $stmt = $this->db->prepare("SELECT * FROM File WHERE CatalogueID = :catalogueID");
            $stmt->bindParam(':catalogueID', $catalogueID, PDO::PARAM_INT);
            $stmt->execute();

            while ($file = $stmt->fetchObject("File")) {
                $files[] = $file;
            }
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return $files;
    }

    public function countAllFiles(): int
    {
        $files = array();
        try {
            $stmt = $this->db->query("SELECT FileID FROM File");
            $stmt->execute();
            while ($file = $stmt->fetchObject("File")) {
                $files[] = $file;
            }
            $count = count($files);
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return $count;
    }


    public function isFileOwner(int $fileID, int $userID): File // TODO change return in interface
    {
        try {
            $stmt = $this->db->prepare("SELECT * FROM File WHERE FileID = :fileID AND UserID = :userID");
            $stmt->bindParam(':fileID', $fileID, PDO::PARAM_INT);
            $stmt->bindParam(':userID', $userID, PDO::PARAM_INT);
            $stmt->execute();
            return $stmt->fetchObject("File");
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
        //return $file; // TODO: get user id from objcet
    }


    public function fetchAuthor(int $userID): string
    {
        try {
            $stmt = $this->db->prepare("SELECT Author FROM User WHERE UserID = :userID");
            $stmt->bindParam(':userID', $userID, PDO::PARAM_INT);
            $stmt->execute();
            return $stmt->fetch(PDO::PARAM_STR);
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function showLatestFiles(int $access): array {
        if ($access == 1) {
            // Return all
            $files = array();
            try {
                $stmt = $this->db->prepare("SELECT * FROM File ORDER BY Date DESC LIMIT 5");
                $stmt->execute();
                while ($file = $stmt->fetchObject("File")) {
                    $files[] = $file;
                }
            } catch (Exception $e) {
                print $e->getMessage() . PHP_EOL;
            }
            return $files;
        } else if ($access == 0) {
            // Return open files
            $files = array();
            try {
                $stmt = $this->db->prepare("SELECT * FROM File WHERE Access = :access ORDER BY Date DESC LIMIT 5");
                $stmt->bindParam(':access', $access);
                $stmt->execute();
                while ($file = $stmt->fetchObject("File")) {
                    $files[] = $file;
                }
            } catch (Exception $e) {
                print $e->getMessage() . PHP_EOL;
            }
            return $files;
        }
    }

    public function showUsersFiles(int $UserID): array
    {
        $files = array();
        try {
            $stmt = $this->db->prepare("SELECT * FROM File WHERE UserID = :userID");
            $stmt->bindParam(':userID', $UserID);
            $stmt->execute();
            while ($file = $stmt->fetchObject("File")) {
                $files[] = $file;
            }
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return $files;
    }

    public function fetchFileCatalogue(int $CatalogueID): string
    {
        try {
            $stmt = $this->db->prepare("SELECT Name FROM Catalogue WHERE CatalogueID = :catalogueID");
            $stmt->bindParam(':catalogueID', $CatalogueID, PDO::PARAM_INT);
            $stmt->execute();

            return $stmt->fetch(PDO::PARAM_STR);
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function fetchLast5():array{
        try {
            $files = array();
            $stmt = $this->db->prepare("SELECT * FROM File ORDER BY FileID LIMIT 5");
            $stmt->execute();
            while ($file = $stmt->fetchObject("File")) {
                $files[] = $file;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return $files;
    }
}