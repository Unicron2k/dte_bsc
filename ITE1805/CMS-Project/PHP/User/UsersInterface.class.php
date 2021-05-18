<?php
interface UsersInterface {
    public function showAll() : array;                          // Returnerer array med User referanser
    public function showAllFiles(User $user) : array;           // Returnerer array med alle filer for en user
    public function showUser(int $id) : ?User;// Returnerer referanse til User med angitt id
    public function showUserByName(string $username) : ?User;
    public function addUser(User $user) : int;              // Returnerer id for nyopprettet User
    public function updateUser(User $user, int $id) : bool;   // Returnerer true eller false avhengig av om oppdateringen var vellykket eller ikke
    public function verifyUser($verificationKey) : bool; //Returnerer true elelr false avhengig ac om brukeren ble bekreftet eller ikke
}