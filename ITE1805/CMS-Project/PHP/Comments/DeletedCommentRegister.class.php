<?php
class DeletedCommentRegister implements DeletedCommentInterface {
    private $db;

    public function __construct(PDO $db) {
        $this->db = $db;
    }

    public function showAllDeletedComments(): array
    {
        // Return all deleted comments from table `DeletedComments`
        $deletedComments = array();
        try {
            $stmt = $this->db->prepare("SELECT * FROM DeletedComments ORDER BY DateDeleted DESC");
            $stmt->execute();
            while ($deletedComment = $stmt->fetchObject("DeletedComment")) {
                $deletedComments = $deletedComment;
            }
            return $deletedComments;
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return $deletedComments;
    }

    public function showAllDeletedCommentsFromUser(int $userID): array
    {
        // Return all deleted comments from a user
        $deletedComments = array();
        try {
            $stmt = $this->db->prepare("SELECT * FROM DeletedComments where UserID = :userID");
            $stmt->bindParam(':userID', $userID, PDO::PARAM_INT);
            $stmt->execute();

            while ($deletedComment = $stmt->fetchObject("DeletedComment")) {
                $deletedComments = $deletedComment;
            }
            return $deletedComments;
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return $deletedComments;
    }

    public function showDeletedComment(int $deletedCommentID): DeletedComment
    {
        // Return specific deleted comment from table `DeletedComments`
        try {
            $stmt = $this->db->prepare("SELECT * FROM DeletedComments WHERE DeletedCommentID = :deletedCommentID");
            $stmt->bindParam(':deletedCommentID', $deletedCommentID, PDO::PARAM_INT);
            $stmt->execute();

            return $stmt->fetchObject("DeletedComment");
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }
}