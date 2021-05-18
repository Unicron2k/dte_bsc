<?php
interface DeletedCommentInterface {
    public function showAllDeletedComments(): array;                            // Return array with all deleted comments
    public function showAllDeletedCommentsFromUser(int $userID) : array;        // Return array with specific users deleted comments
    public function showDeletedComment(int $deletedCommentID): DeletedComment;  // Return specific deleted comment based on it's ID in table `DeletedComments`
}