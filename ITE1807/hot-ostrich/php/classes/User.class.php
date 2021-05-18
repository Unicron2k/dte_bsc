<?php
class User
{
    private String $username;
    private String $email;
    private String $password;
    private String $firstName;
    private String $lastName;
    private String $lastLogin;

    private int $idUser;
    private int $employeeNumber;
    private int $numLogin;
    private int $Position_idPosition;
    private int $Role_idRole;
    private int $Fakultet_idFakultet;

    private bool $loggedIn;

    function __construct(){

    }

    /**
     * Returns Username of the current User.
     * @return String
     */
    public function getUsername(): String
    {
        return $this->username;
    }

    /**
     * Sets username locally for the session.
     * @param String $username
     */
    public function setUsername(String $username): void
    {
        $this->username = $username;
    }

    /**
     * Returns Email of the current User.
     * @return String
     */
    public function getEmail(): String
    {
        return $this->email;
    }

    /**
     * Sets Email locally for the session.
     * @param String $email
     */
    public function setEmail(String $email): void
    {
        $this->email = $email;
    }

    /**
     * Returns password Hash of the current User.
     * @return String
     */
    public function getPassword(): String
    {
        return $this->password;
    }

    /**
     * Sets password Hash locally for the session.
     * @param String $password
     */
    public function setPassword(String $password): void
    {
        $this->password = $password;
    }

    /**
     * @return String
     */
    public function getFirstName(): String
    {
        return $this->firstName;
    }

    /**
     * Sets FirstName locally for the session.
     * @param String $firstName
     */
    public function setFirstName(String $firstName): void
    {
        $this->firstName = $firstName;
    }

    /**
     * @return String
     */
    public function getLastName(): String
    {
        return $this->lastName;
    }

    /**
     * Sets LastName locally for the session.
     * @param String $lastName
     */
    public function setLastName(String $lastName): void
    {
        $this->lastName = $lastName;
    }

    /**
     * @return String
     */
    public function getLastLogin(): String
    {
        return $this->lastLogin;
    }

    /**
     * Sets LastLogin locally for the session.
     * @param String $lastLogin
     */
    public function setLastLogin(String $lastLogin): void
    {
        $this->lastLogin = $lastLogin;
    }

    /**
     * @return int
     */
    public function getIdUser(): int
    {
        return $this->idUser;
    }

    /**
     * Sets userId locally for the session.
     * @param int $idUser
     */
    public function setIdUser(int $idUser): void
    {
        $this->idUser = $idUser;
    }

    /**
     * @return int
     */
    public function getEmployeeNumber(): int
    {
        return $this->employeeNumber;
    }

    /**
     * Sets EmployeeNumber locally for the session.
     * @param int $employeeNumber
     */
    public function setEmployeeNumber(int $employeeNumber): void
    {
        $this->employeeNumber = $employeeNumber;
    }

    /**
     *
     * @return int
     */
    public function getNumLogin(): int
    {
        return $this->numLogin;
    }

    /**
     * Sets Number Of logins locally for the session.
     * @param int $numLogin
     */
    public function setNumLogin(int $numLogin): void
    {
        $this->numLogin = $numLogin;
    }

    /**
     * @return int
     */
    public function getPositionIdPosition(): int
    {
        return $this->Position_idPosition;
    }

    /**
     * Sets the users position Id locally for the session.
     * @param int $Position_idPosition
     */
    public function setPositionIdPosition(int $Position_idPosition): void
    {
        $this->Position_idPosition = $Position_idPosition;
    }

    /**
     * @return int
     */
    public function getRoleIdRole(): int
    {
        return $this->Role_idRole;
    }

    /**
     * @return int
     */
    public function getFakultetIdFakultet(): int
    {
        return $this->Fakultet_idFakultet;
    }

    /**
     * @param int $Fakultet_idFakultet
     */
    public function setFakultetIdFakultet(int $Fakultet_idFakultet): void
    {
        $this->Fakultet_idFakultet = $Fakultet_idFakultet;
    }

    /**
     * TODO: Evaluate if this feature is needed.
     * Returns current login status to verify if the user is logged in.
     * @return bool
     */
    public function isLoggedIn(): bool
    {
        return $this->loggedIn;
    }

    /**
     * TODO: Evaluate if this feature is needed.
     * Sets the login status between true and false.
     * Boolean login status, needs to be true for the user to access any of the sites functionality.
     * @param bool $loggedIn
     */
    public function setLoggedIn(bool $loggedIn): void
    {
        $this->loggedIn = $loggedIn;
    }

    /**
     * Joins FirstName and LastName together with a space and returns it.
     * @return string
     */
    public function getFullName(){
        return $fullname = ($this->getFirstName() . ' ' .  $this->getLastName());
    }

    /**
     * Unset's session when method is called.
     */
    public function logOut(){
        unset ($_SESSION['bruker']);
        $this->loggedIn = false;
    }

}
?>
