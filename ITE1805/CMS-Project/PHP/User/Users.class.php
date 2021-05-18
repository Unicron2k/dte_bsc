<?php
class Users implements UsersInterface {
	private $db;
	
	public function __construct(PDO $db) {
		$this->db = $db;
	}


	public function showAll(): array {
		$users = array();

        try {
            $stmt = $this->db->prepare("SELECT * FROM `User` ORDER BY UserID DESC ");
            $stmt->execute();
            while ($user = $stmt->fetchObject("User")) {
                $users[] = $user;
            }
            return $users;
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
		return $users;
    }

    public function showAllFiles(User $user): array {
        $files = array();

        try {
            $stmt = $this->db->prepare("SELECT * FROM File WHERE UserID= :userID ORDER BY `Date` DESC");
            $stmt->bindParam(':userID', $user->hentId(), PDO::PARAM_INT);
            $stmt->execute();

            while ($file = $stmt->fetchObject("File")) {
                $files[] = $file;
            }
            return $files;
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return $files;
    }

	public function showUser(int $id) : ?User {
        try {
            $stmt = $this->db->prepare("SELECT * FROM `User` WHERE UserID = :id");
            $stmt->bindParam(":id", $id, PDO::PARAM_INT);
            $stmt->execute();
            $result = $stmt->fetchObject("User");

            if(!empty($result)) {
                return $result;
            } else {
                return null;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return null;
    }

    public function showUserByName(string $username) : ?User {
        try {
            $stmt = $this->db->prepare("SELECT * FROM `User` WHERE Username = :username");
            $stmt->bindParam(":username", $username, PDO::PARAM_INT);
            $stmt->execute();
            $result = $stmt->fetchObject("User");
            if($result) {
                return $result;
            } else {
                return null;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function addUser(User $user) : int {
        try {
            $stmt = $this->db->prepare("INSERT INTO `User` (Username, Email, PassHash, VerificationKey, FirstName, LastName) VALUES (:username,:email,:passHash,:verificationKey,:firstName,:lastName)");
            $stmt->bindValue(':username', $user->getUsername(), PDO::PARAM_STR);
            $stmt->bindValue(':email', $user->getEmail(), PDO::PARAM_STR);
            $stmt->bindValue(':passHash', $user->getPassHash(), PDO::PARAM_STR);
            $stmt->bindValue(':verificationKey', $user->getVerificationKey(), PDO::PARAM_STR);
            $stmt->bindValue(':firstName', $user->getFirstName(), PDO::PARAM_STR);
            $stmt->bindValue(':lastName', $user->getLastName(), PDO::PARAM_STR);

            $result = $stmt->execute();

            if ($result) {
                return true;
            } else {
                echo "Feil ved innlegging av ny bruker!";
                return false;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function updateUser(User $user, int $id) : bool {
        try {
            $stmt = $this->db->prepare("UPDATE User SET Username= :username, Email= :email, FirstName= :firstName, LastName= :lastName WHERE UserID =:id"); // TODO -> Skal vi ha med 'SET PassHash' her?
            $stmt->bindParam(':id', $id, PDO::PARAM_INT);
            $stmt->bindValue(':username', $user->getUsername(), PDO::PARAM_STR);
            $stmt->bindValue(':email', $user->getEmail(), PDO::PARAM_STR);
            $stmt->bindValue(':firstName', $user->getFirstName(), PDO::PARAM_STR);
            $stmt->bindValue(':lastName', $user->getLastName(), PDO::PARAM_STR);
            $result = $stmt->execute();

            if ($result) {
                return true;
            } else {
                echo "Feil ved oppdatering av post!";
                return false;
            }
        } catch(Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function verifyUser($verificationKey) : bool {

        //TODO: verify if key actually exist!
	    //First we get the UserID associated with the verification key
        $stmt = $this->db->prepare("select `UserID` from `User` where `VerificationKey` =:verificationKey");
        $stmt->bindParam(':verificationKey', $verificationKey, PDO::PARAM_INT);
        $result = $stmt->execute();

        $id = $stmt->fetch(PDO::FETCH_ASSOC)['UserID'];

        //Then we update the associated user and mark them as verified
        $stmt = $this->db->prepare("UPDATE `User` SET `Verified` = '1' WHERE `UserID` =:id");
        $stmt->bindParam(':id', $id, PDO::PARAM_INT);
        $result = $stmt->execute();

        if ($result)
            return true;
        else
            return false;
    }
}