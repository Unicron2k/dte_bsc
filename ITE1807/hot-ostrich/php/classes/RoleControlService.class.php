<?php


class RoleControlService
{
    private PDO $db;
    private array $errorMsgs;


    function __construct(PDO $db)
    {
        $this->db = $db;
    }

    /**
     * @param $userRoleId
     * @return RoleControl|null
     */
    public function getUserRole($userRoleId) : ?RoleControl {
        try{
            //Return NULL if $userRoleId is less than 1
            if(!is_numeric($userRoleId) || $userRoleId < 1){
                $this->errorMsgs = array("\$idCourseCode: " . $userRoleId . ": invalid number");
                return NULL;
            }
                $stmt = $this->db->prepare("SELECT * FROM Role WHERE idRole=:userRoleId");
                $stmt->bindParam(':userRoleId', $userRoleId, PDO::PARAM_INT);

                if($stmt->execute()){
                    $result = $stmt->fetchObject("RoleControl");
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
     * @return array|null
     */
    public function getAllRoles(): ?array
    {
        $arrRoles = array();
        try {
            // No userdata used, no real need to sanitize...
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `Role` order by`idRole`;");

            //Execute query, and set return-status + any potential error-message
            if ($stmt->execute()) {
                //Fetch CourseCode-objects from query-results
                while ($Role = $stmt->fetchObject("RoleControl")) {
                    $arrRoles[] = $Role;
                }
                return $arrRoles;
            } else {
                $this->errorMsgs = $stmt->errorInfo();
                return NULL;
            }

        } catch (Exception $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }

        return NULL;
    }


    /**
     * @param $newRole
     * @param $IdUser
     * @return null
     */
    public function editRoleById($newRole, $IdUser){
        if(!is_numeric($newRole) || $newRole < 1){
            $this->errorMsgs = array("\$idCourseCode: " . $newRole . ": invalid number");
            return NULL;
        }
        $stmt = $this->db->prepare("UPDATE User SET Role_idRole=:userRoleId WHERE idUser=:idUser ");
        $stmt->bindParam(':idUser', $IdUser, PDO::PARAM_INT);
        $stmt->bindParam(':userRoleId', $newRole, PDO::PARAM_INT);
        $stmt->execute() or print_r($stmt->errorInfo());
        return;
    }


    /**
     * @param $arrAllRoles
     * @return null
     */
    public function updateAllRoles($arrAllRoles) : bool {
        try {
            foreach ($arrAllRoles as $arrRoles) {
                $stmt = $this->db->prepare("UPDATE `Role` SET `read` = ?, `write` = ?, `edit` = ?, `delete` = ?, `create` = ?, `approve` = ? WHERE `Role`.`idRole` = ?;");
                if (!$stmt->execute($arrRoles)) {
                    $this->errorMsgs[] = "Could not update permissions for idRole " . $arrRoles[sizeof($arrRoles)-1];
                    $this->errorMsgs[] = $stmt->errorInfo();
                }
            }
            return true;
        } catch (InvalidArgumentException $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }
        return false;
    }

}
