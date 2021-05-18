<?php
interface CommentInterface {
    public function showAllComments(): array;                           // Return array with all comments
    public function showAllCommentsFromFile(int $fileID): Array;        // Return array with all comments in certain File
    public function showComment(int $commentID): Comment;               // Return specific comment
    public function addComment(Comment $comments) : int;                // Return id for new comment
    public function deleteComment(int $commentID): bool;                // Return bool for status of comment deletion
    public function deleteAllCommentsFromFile(int $fileID) : bool;      // Return bool for status of deletion of all comments from certain File
    //public function updateComment(comment $comment, int $id): bool;   // Return bool for status of comment update
}