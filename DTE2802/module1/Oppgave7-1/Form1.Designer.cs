namespace Oppgave7
{
    partial class formRegEx
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.richTextBoxFrom = new System.Windows.Forms.RichTextBox();
            this.textBoxRegEx = new System.Windows.Forms.TextBox();
            this.richTextBoxTo = new System.Windows.Forms.RichTextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.buttonSplit = new System.Windows.Forms.Button();
            this.buttonMatches = new System.Windows.Forms.Button();
            this.buttonClear = new System.Windows.Forms.Button();
            this.buttonReplace = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // richTextBoxFrom
            // 
            this.richTextBoxFrom.Location = new System.Drawing.Point(12, 23);
            this.richTextBoxFrom.Name = "richTextBoxFrom";
            this.richTextBoxFrom.Size = new System.Drawing.Size(537, 96);
            this.richTextBoxFrom.TabIndex = 0;
            this.richTextBoxFrom.Text = "Dette1er2en3test4på5bruk6av7regulære8uttrykk. Her er mer tekst, bruker også komma" +
                ".";
            // 
            // textBoxRegEx
            // 
            this.textBoxRegEx.Font = new System.Drawing.Font("Courier New", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.textBoxRegEx.Location = new System.Drawing.Point(108, 136);
            this.textBoxRegEx.Name = "textBoxRegEx";
            this.textBoxRegEx.Size = new System.Drawing.Size(197, 20);
            this.textBoxRegEx.TabIndex = 1;
            this.textBoxRegEx.Text = "\\d+|\\s+|, +|,+|\\. +|\\.+";
            // 
            // richTextBoxTo
            // 
            this.richTextBoxTo.BackColor = System.Drawing.SystemColors.ButtonFace;
            this.richTextBoxTo.Location = new System.Drawing.Point(13, 176);
            this.richTextBoxTo.Name = "richTextBoxTo";
            this.richTextBoxTo.ReadOnly = true;
            this.richTextBoxTo.Size = new System.Drawing.Size(536, 220);
            this.richTextBoxTo.TabIndex = 2;
            this.richTextBoxTo.Text = "";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(13, 136);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(89, 13);
            this.label1.TabIndex = 3;
            this.label1.Text = "Regulært uttrykk:";
            // 
            // buttonSplit
            // 
            this.buttonSplit.Location = new System.Drawing.Point(311, 136);
            this.buttonSplit.Name = "buttonSplit";
            this.buttonSplit.Size = new System.Drawing.Size(75, 23);
            this.buttonSplit.TabIndex = 4;
            this.buttonSplit.Text = "Splitt";
            this.buttonSplit.UseVisualStyleBackColor = true;
            this.buttonSplit.Click += new System.EventHandler(this.buttonSplit_Click);
            // 
            // buttonMatches
            // 
            this.buttonMatches.Location = new System.Drawing.Point(392, 136);
            this.buttonMatches.Name = "buttonMatches";
            this.buttonMatches.Size = new System.Drawing.Size(75, 23);
            this.buttonMatches.TabIndex = 5;
            this.buttonMatches.Text = "Matches";
            this.buttonMatches.UseVisualStyleBackColor = true;
            this.buttonMatches.Click += new System.EventHandler(this.buttonMatches_Click);
            // 
            // buttonClear
            // 
            this.buttonClear.Location = new System.Drawing.Point(474, 136);
            this.buttonClear.Name = "buttonClear";
            this.buttonClear.Size = new System.Drawing.Size(75, 23);
            this.buttonClear.TabIndex = 6;
            this.buttonClear.Text = "Clear";
            this.buttonClear.UseVisualStyleBackColor = true;
            this.buttonClear.Click += new System.EventHandler(this.buttonClear_Click);
            // 
            // buttonReplace
            // 
            this.buttonReplace.Location = new System.Drawing.Point(556, 136);
            this.buttonReplace.Name = "buttonReplace";
            this.buttonReplace.Size = new System.Drawing.Size(75, 23);
            this.buttonReplace.TabIndex = 7;
            this.buttonReplace.Text = "Replace";
            this.buttonReplace.UseVisualStyleBackColor = true;
            this.buttonReplace.Click += new System.EventHandler(this.buttonReplace_Click);
            // 
            // formRegEx
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(587, 408);
            this.Controls.Add(this.buttonClear);
            this.Controls.Add(this.buttonMatches);
            this.Controls.Add(this.buttonSplit);
            this.Controls.Add(this.buttonReplace);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.richTextBoxTo);
            this.Controls.Add(this.textBoxRegEx);
            this.Controls.Add(this.richTextBoxFrom);
            this.Name = "formRegEx";
            this.Text = "Bruk av Regular Expression";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.RichTextBox richTextBoxFrom;
        private System.Windows.Forms.TextBox textBoxRegEx;
        private System.Windows.Forms.RichTextBox richTextBoxTo;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button buttonSplit;
        private System.Windows.Forms.Button buttonMatches;
        private System.Windows.Forms.Button buttonClear;
        private System.Windows.Forms.Button buttonReplace;
    }
}

