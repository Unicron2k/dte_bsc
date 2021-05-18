<?php
class CommentRegister implements CommentInterface {
    private $db;

    public function __construct(PDO $db) {
        $this->db = $db;
    }

    public function showAllComments(): array
    {
        // Return all comments from table `Comments`
        $comments = array();
        try {
            $stmt = $this->db->query("SELECT * FROM Comments ORDER BY `Date` DESC"); // TODO: FIX like File and Catalogue
            $stmt->execute();

            while ($comment = $stmt->fetchObject("Comment")) {
                $comments[] = $comment;
            }
            return $comments;
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return $comments;
    }

    public function showAllCommentsFromFile(int $fileID): Array
    {
        // Return all comments in given File by comment's FileID
        $comments = array();
        try {
            $stmt = $this->db->prepare("SELECT * FROM Comments WHERE FileID = :fileID ORDER BY Date");
            $stmt->bindParam(':fileID', $fileID, PDO::PARAM_INT);
            $stmt->execute();

            while ($comment = $stmt->fetchObject("Comment")) {
                $comments[] = $comment;
            }
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return $comments;
    }

    public function showComment(int $commentID): Comment
    {
        // Return specific comment from table `Comments`
        try {
            $stmt = $this->db->prepare("SELECT * FROM Comments WHERE CommentID = :commentID");
            $stmt->bindParam(':commentID', $commentID, PDO::PARAM_INT);
            $stmt->execute();
            return $stmt->fetchObject("Comment");
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function addComment(Comment $comment): int
    {
        // Add comment to table `Comments` if under 250 in length
        if (strlen($comment->getComment()) < 250) {
            try {
                $stmt = $this->db->prepare("INSERT INTO `Comments`(`FileID`, `UserID`, `Date`, `Comment`) VALUES (:fileID, :userID, `:date`, :comment)");
                $stmt->bindValue(':fileID', $comment->getFileID(), PDO::PARAM_INT);
                $stmt->bindValue(':userID', $comment->getUserID(), PDO::PARAM_INT);
                $stmt->bindValue(':date', $comment->getDate(), PDO::PARAM_STR);
                $stmt->bindValue(':comment', $comment->getComment(), PDO::PARAM_STR);
                $result = $stmt->execute();

                if ($result) {
                    return true;
                } else {
                    echo "Feil ved innlegging av ny comment!";
                    return false;
                }
            } catch (InvalidArgumentException $e) {
                print $e->getMessage() . PHP_EOL;
            }
        } else {
            echo "For lang comment! Maks lengde er 250";
        }
    }

    public function deleteComment(int $commentID): bool
    {
        // Delete specific comment by it's ID in table `Comments`
        try {
            // Delete $comment from table `Comments`
            $stmt = $this->db->prepare("DELETE FROM Comments WHERE CommentID = :commentID");
            $stmt->bindParam(':commentID', $commentID, PDO::PARAM_INT);
            $result = $stmt->execute();

            if ($result) {
                return true;
            } else {
                echo "Feil ved sletting av comment!";
                return false;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }

    public function deleteAllCommentsFromFile(int $fileID): bool
    {
        // Delete all comments in table `Comments` with certain FileID
        try {
            $stmt = $this->db->prepare("DELETE FROM Comments Where FileID = :fileID");
            $stmt->bindParam(':fileID', $fileID, PDO::PARAM_INT);
            $result = $stmt->execute();

            if ($result)
                return true;
            else {
                echo "Feil ved sletting av Comments i gitt File";
                return false;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }
}