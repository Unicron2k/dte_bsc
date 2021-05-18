<?php


class FakultetService
{


    function __construct(PDO $db)
    {
        $this->db = $db;
    }

    /**
     * @param $userFakultetId
     * @return Fakultet|null
     */
    public function getFakultet($userFakultetId) : ?Fakultet {
        try{
            //Return NULL if $userFakultetId is less than 1
            if(!is_numeric($userFakultetId) || $userFakultetId < 1){
                $this->errorMsgs = array("\$idCourseCode: " . $userFakultetId . ": invalid number");
                return NULL;
            }
            $stmt = $this->db->prepare("SELECT * FROM Fakultet WHERE idFakultet=:userFakultetId");
            $stmt->bindParam(':userFakultetId', $userFakultetId, PDO::PARAM_INT);

            if($stmt->execute()){
                $result = $stmt->fetchObject("Fakultet");
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


    public function getAllFakultet(): ?array
    {
        $arrFakultet = array();
        try {
            // No userdata used, no real need to sanitize...
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `Fakultet` order by`idFakultet`;");

            //Execute query, and set return-status + any potential error-message
            if ($stmt->execute()) {
                //Fetch CourseCode-objects from query-results
                while ($Fakultet = $stmt->fetchObject("Fakultet")) {
                    $arrFakultet[] = $Fakultet;
                }
                return $arrFakultet;
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
     * @param $newFakultet
     * @param $IdUser
     * @return null
     */
    public function editFakultetById($newFakultet, $IdUser){
        if(!is_numeric($newFakultet) || $newFakultet < 1){
            $this->errorMsgs = array("\$idCourseCode: " . $newFakultet . ": invalid number");
            return NULL;
        }
        $stmt = $this->db->prepare("UPDATE User SET Fakultet_idFakultet=:userFakultetId WHERE idUser=:idUser ");
        $stmt->bindParam(':idUser', $IdUser, PDO::PARAM_INT);
        $stmt->bindParam(':userFakultetId', $newFakultet, PDO::PARAM_INT);
        $stmt->execute() or print_r($stmt->errorInfo());
    }


}