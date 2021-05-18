<?php
interface FileInterface {
    public function showAllFiles(int $access) : array;                  // Return array of all Files
    public function showFile(int $id) : File;                           // Return given File by it's ID
    public function addFile(File $file, int $UserID);                   // Return ID for added File
    public function updateFile(File $file, int $id) : bool;             // Return bool for status for updating File
    public function deleteFile(int $id) : bool;                         // Return bool for status of deleting File
    public function showAllFilesInCatalogue(int $catalogueID) : Array;  // Return array of all files in given Catalogue
    public function countAllFiles() : int;                              // Return number of files in database as int
    public function isFileOwner(int $fileID, int $userID) : File;       // Return File and check
    public function fetchAuthor(int $userID) : string;                  // Return author for given user as string
    public function showLatestFiles(int $access): array;                // Return array of latest 5 files
    public function showUsersFiles(int $UserID): array;                 // Return all files owned by user
    public function fetchFileCatalogue(int $CatalogueID): string;       // Return Catalogue as string in a File
    public function fetchLast5(): array;
}