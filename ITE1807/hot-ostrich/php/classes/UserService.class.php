<?php


class UserService
{
    private PDO $db;
    private array $errorMsgs;


    function __construct(PDO $db)
    {
        $this->db = $db;
    }

    /**
     * Unset's session when method is called.
     */
    public function logOut()
    {
        unset ($_SESSION['bruker']);
        $this->loggedIn = false;
    }

    /**
     * Updates number of logins and time of login after login.
     * @param $db
     */
    public function UpdateUserInfo($db, $username)
    {
        $prep = $db->prepare("UPDATE User SET numLogin = numLogin + 1, lastLogin= :logintime WHERE username=:uname OR email=:uname");
        $date = date('Y-m-d H:i:s', $_SERVER['REQUEST_TIME']);
        $prep->bindParam(':uname', $username, PDO::PARAM_STR);
        $prep->bindParam(':logintime', $date, PDO::PARAM_STR);
        $prep->execute() or die(print_r($prep->errorInfo(), false));
    }

    /**
     * Login Query, addresses user details and creates user object on success.
     * Read Usage Doc in User.class.
     * @param PDO $db
     * @param $usernameEmail
     * @param $password
     * @return bool
     */

    public function login(PDO $db, $usernameEmail, $password): ?User{

        $prep = $db->prepare("SELECT username, email FROM User WHERE username=:uname OR email=:umail");
        $prep->execute(array(':uname' => $usernameEmail, ':umail' => $usernameEmail));
        $row = $prep->fetch(PDO::FETCH_ASSOC);

        if (!$row['username'] || !$row['email'] == $usernameEmail) {
            echo '<p class="alert-danger">User does not exist, please try again or register an account.</p>';
            return NULL;
        }

        $stmt = $db->prepare("SELECT * FROM User WHERE username=:usernameEmail OR email=:usernameEmail");
        $stmt->bindParam(':usernameEmail', $usernameEmail, PDO::PARAM_STR);

        if ($stmt->execute()) {
            $result = $stmt->fetchObject("User");
            if (password_verify($password, $result->getPassword())) {
                $this->UpdateUserInfo(DB::getDBConnection(), $usernameEmail);
                $result->setLoggedIn(true);
                    if ($result) {
                        return $result;
                        } else {
                            $this->errorMsgs = $stmt->errorInfo();
                            return NULL;
                        }
                } else {
                    echo '<p class="alert-danger">Incorrect password, please try again</p>';
                    $this->errorMsgs = $stmt->errorInfo();
                    return NULL;
                }
            }
        else {
            $this->errorMsgs = $stmt->errorInfo();
            return NULL;
        }
    }

    /**
     * Fetch a user by idUser
     * Returns a User-object if successful, null if failed.
     * Call GetLastError to get error-message.
     * @param int $idUser ID of user to fetch
     * @return User|null Returns a User-object if succeeded, null if failed. In case of false, call GetLastError to get last error-message
     */
    public function getUser(int $idUser) : ?User {
        try{
            //Return NULL if $idCourseLeader is less than 1
            if(!is_numeric($idUser) || $idUser < 1){
                $this->errorMsgs = array("\$idUser: " . $idUser . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            // TODO: change from * to what is minimally needed to display a user
            // FIXME: change to use proper  before production
            $stmt = $this->db->prepare("select * from `User` where `User`.`idUser` = :idUser");
            $stmt->bindParam(":idUser", $idUser, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result = $stmt->fetchObject("User");
                if($result){
                    return $result;
                } else {
                    $this->errorMsgs = $stmt->errorInfo();
                    return NULL;
                }
            } else {
                $this->errorMsgs = $stmt->errorInfo();
                return NULL;
            }

        } catch (InvalidArgumentException $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }

        return NULL;
    }

    /**
     * Fetch all users.
     * Returns an array containing all user-objects, null if failed.
     * Call GetLastError to get error-messages
     * @return array|null Array containing all User-objects if succeeded, null if failed. In case of failure, call GetLastError to get last error message
     */
    public function getAllUsers() : ?array {
        try{
            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `User`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                while ($user = $stmt->fetchObject("User")) {
                    $arrUsers[] = $user;
                }
                return $arrUsers;
            } else {
                $this->errorMsgs = $stmt->errorInfo();
                return NULL;
            }
        } catch (InvalidArgumentException $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }

        return NULL;
    }

    /**
     * Returns array $errorMsgs
     * @return array Array containing the last generated error-message
     */
    public function getLastError() : array {
        // TODO: Format array as a single string
        return $this->errorMsgs;
    }
}