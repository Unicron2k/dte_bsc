-- Generate this trigger in table `Comments`


# Trigger moves relevant information from row in `Comments` to `DeletedComments` on deletion along with timestamp of deletion
DELIMITER $$
CREATE TRIGGER MoveCommentOnDeletion
AFTER DELETE ON Comments
FOR EACH ROW
BEGIN

INSERT INTO DeletedComments(CommentID, UserID, DeletedComment, DateDeleted) VALUES (old.CommentID, old.UserID, old.Comment, CURRENT_TIMESTAMP);

END$$
DELIMITER ;