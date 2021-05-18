// Please see documentation at https://docs.microsoft.com/aspnet/core/client-side/bundling-and-minification
// for details on configuring this project to bundle and minify static web assets.

// Write your JavaScript code.
async function getComments(postId = 0, locked = false) {
    const response = await fetch('/api/comment/' + postId);
    const jsonResult = await response.json();
    let commentSection = document.getElementById("comments")
    for (let key in jsonResult) {
        if (jsonResult.hasOwnProperty(key)) {
            let comment = jsonResult[key];
            let hr = document.createElement("hr");
            let newComment = document.createElement("div");
            let replyTo = comment["replyTo"] > 0 ? " replied to " + comment["replyTo"] + ":" : " wrote:";
            newComment.innerHTML = "<h6>On " + comment["postDate"] + ", " + comment["commentId"] + replyTo + "</h6>" +
                "<p>" + comment["content"] + "</p>" +
                "<div>" +
                "<a href='javascript:newComment(" + comment["commentId"] + ")'>Reply to</a>" +
                "<text> | </text>" +
                "<a href='javascript:editComment(" + comment["commentId"] + ")' >Edit</a>" +
                "<text> | </text>" +
                "<a href='javascript:deleteComment(" + comment["commentId"] + ");'>Delete</a>" +
                "</div>";
            commentSection.appendChild(newComment);
            commentSection.appendChild(hr);
        }
    }
    if (!locked) {
        let addComment = document.createElement("a");
        addComment.id = "newComment";
        addComment.href = "#";
        addComment.innerHTML = "Add comment";
        commentSection.appendChild(addComment);

        var modal = document.getElementById("newCommentModal");
        var btnNewComment = document.getElementById("newComment");
        var span = document.getElementsByClassName("close")[0];
        var submit = document.getElementById("submit");
        var cancel = document.getElementById("cancel");
        btnNewComment.onclick = function() {
            modal.style.display = "block";
        }
        span.onclick = function() {
            modal.style.display = "none";
        }
        cancel.onclick = function() {
            modal.style.display = "none";
        }
        submit.onclick = function() {
            modal.style.display = "none";
            let newCommentContent = document.getElementById("newCommentContent").value;
            submitComment(postId, 0, newCommentContent);
        }
        window.onclick = function(event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        }
    }
}

function submitComment(postId=0, replyTo=0, content = ""){
    let formData = {"BlogPostId": postId,"Content": content,"ReplyTo": replyTo};
    $.ajax({
        url : "/api/comment",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data : JSON.stringify(formData),
        success: function(data, textStatus, jqXHR)
        {
            //data - response from server
            location.reload();
        },
        error: function (jqXHR, textStatus, errorThrown)
        {
            alert("Something went wrong. Please try again later.")
        }
    });
}