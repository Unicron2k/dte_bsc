"use strict";
class Stack {
    constructor() {
        this.matrixStack = [];
    }

    // Add matrix to stack
    pushMatrix(matrix) {
        let copyToPush = new Matrix4(matrix);
        this.matrixStack.push(copyToPush);
    }

    // Pop the top element
    popMatrix() {
        if (this.matrixStack.length === 0)
            throw "Error in popMatrix(): stack is empty!";
        this.matrixStack.pop();
    }
    a
    // Peeks the top element
    peekMatrix() {
        if (this.matrixStack.length === 0)
            throw "Error in peekMatrix(): stack is empty!";
        return new Matrix4(this.matrixStack[this.matrixStack.length - 1]);
    }

    // Empties the stack
    empty() {
        while (this.matrixStack.length > 0)
            this.matrixStack.pop();
    }
}